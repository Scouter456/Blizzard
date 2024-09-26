package com.scouter.blizzard.biomelevels;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.scouter.blizzard.events.BlizzardData;
import com.scouter.blizzard.events.BlizzardWorldData;
import com.scouter.blizzard.util.CodecUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BiomeLevelData extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Codec<Map<Long, UUID>> BIOME_CHUNK_MAPPER = Codec.unboundedMap(Codec.LONG, UUIDUtil.CODEC)
            .xmap(Map::copyOf, Map::copyOf)
            .orElse(e -> {
                        LOGGER.error("Failed to parse Chunk Entries can't send packet! Due to " + e);
                    },
                    new ConcurrentHashMap<>());

    private final Multimap<ResourceKey<Biome>, UUID> idMap = HashMultimap.create();
    private final Long2ObjectOpenHashMap<UUID> chunkPosMap = new Long2ObjectOpenHashMap<>();
    //private Map<UUID, ChunkMap> chunkMapMap = new ConcurrentHashMap<>();
    private final Map<UUID, BiomeData> dataMap = new ConcurrentHashMap<>();
    //private final ReentrantLock lock = new ReentrantLock();
    private ServerLevel level;

    public static BiomeLevelData get(Level level){
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        // Get the vanilla storage manager from the level
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        BiomeLevelData data = storage.computeIfAbsent(BiomeLevelData::new, BiomeLevelData::new, "biomeData");
        // Get the blizzardData if it already exists. Otherwise, create a new one.
        data.setDirty();
        data.setLevel((ServerLevel) level);
        return data;
    }

    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public int getBiomeLevel(ServerLevel level, ResourceKey<Biome> biomeResourceKey, ChunkPos pos) {
        if(chunkPosMap.containsKey(pos.toLong())) {
            UUID id = chunkPosMap.get(pos.toLong());
            BiomeData data = dataMap.get(id);
            return data.getLevel();
        } else {
            addToData(level, biomeResourceKey, pos);
            UUID id = chunkPosMap.get(pos.toLong());
            BiomeData data = dataMap.get(id);
            return data.getLevel();
        }
    }

    public void addBiomeLevel(ChunkPos pos) {
        if(chunkPosMap.containsKey(pos.toLong())) {
            UUID id = chunkPosMap.get(pos.toLong());
            BiomeData data = dataMap.get(id);
            data.addLevel();
        }
        setDirty();
    }
    public boolean addToData(ServerLevel serverLevel, ResourceKey<Biome> biome, ChunkPos pos) {
        long posLong = pos.toLong();

        if(chunkPosMap.containsKey(posLong)) {
        LOGGER.error("Chunk with {} is already added!", pos);
        return false;
        }

        Multimap<ResourceKey<Biome>, Long> map = HashMultimap.create();
        Multimap<ResourceKey<Biome>, Long> allocatedMap = HashMultimap.create();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = pos.x + i;
                int z = pos.z + j;
                long l = ChunkPos.asLong(x, z);
                if(chunkPosMap.containsKey(l)) {
                    UUID id = chunkPosMap.get(l);
                    BiomeData data = dataMap.get(id);
                    ResourceKey<Biome> biomeResourceKey = data.getBiomeResourceKey();
                    allocatedMap.put(biomeResourceKey, l);
                } else {
                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getMiddleBlockX(), pos.getMiddleBlockZ());
                    Holder<Biome> biomeHolder = serverLevel.getBiome(pos.getMiddleBlockPosition(y));
                    ResourceKey<Biome> biomeResourceKey = biomeHolder.unwrapKey().get();
                    map.put(biomeResourceKey, l);
                }
            }
        }
        //todo fix only part being merged and not all the chunks with the id
        Set<Long> chunksWithRK = new HashSet<>(map.get(biome));
        Set<Long> allocatedChunks = new HashSet<>(allocatedMap.get(biome));
        Set<Long> chunksToAdd = new HashSet<>();
        BiomeData biomeData = new BiomeData(biome, 1);

        for (long chunkPos : allocatedChunks) {
            UUID id = chunkPosMap.get(chunkPos);
            BiomeData existingData = dataMap.get(id);
            chunksToAdd.addAll(existingData.getChunks());


            //Set uuid here, can be done because they are the same area and same biome, otherwise not possible!!
            //This way the uuid keeps being the same..
            biomeData.setUuid(id);

        }

        allocatedChunks.addAll(chunksToAdd);

        for (long chunkPos : allocatedChunks) {
            UUID id = chunkPosMap.get(chunkPos);
            BiomeData existingData = dataMap.get(id);
            biomeData.averageLevel(existingData.getLevel());
        }
        biomeData.addAllL(chunksWithRK);
        biomeData.addAllL(allocatedChunks);
        biomeData.averageLevelByChunks(chunksWithRK.size() + allocatedChunks.size());
        for(long chunk : chunksWithRK) {
            chunkPosMap.put(chunk, biomeData.getUuid());
        }
        for(long chunk : allocatedChunks) {
            chunkPosMap.put(chunk, biomeData.getUuid());
        }
        idMap.put(biome, biomeData.getUuid());
        dataMap.put(biomeData.getUuid(), biomeData);
        chunkPosMap.containsKey(posLong);
        setDirty();
        return true;
    }


    public BiomeLevelData(){
    }

    public BiomeLevelData(CompoundTag nbt) {
        //todo improve messaging and fix parsing!!!
        //Map<Long, UUID> map = BIOME_CHUNK_MAPPER.parse(NbtOps.INSTANCE, nbt.get("biome_chunks")).result().orElse(new HashMap<>());


        ListTag biomeData = nbt.getList("biome_data", 10);
        for (int i = 0; i < biomeData.size(); i++) {
            CompoundTag biomeDataEntry = biomeData.getCompound(i);
            BiomeData biomeData1 = BiomeData.BIOME_DATA_CODEC.parse(NbtOps.INSTANCE, biomeDataEntry).result().orElse(null);
            dataMap.put(biomeData1.getUuid(), biomeData1);
            idMap.put(biomeData1.getBiomeResourceKey(), biomeData1.getUuid());
            for(long chunk : biomeData1.getChunks()) {
                chunkPosMap.put(chunk, biomeData1.getUuid());
            }
        }

    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        //todo improve messaging and fix parsing!!! map not mapped correctly, giving empty tag
        //CompoundTag biomeChunks = (CompoundTag) BIOME_CHUNK_MAPPER.encodeStart(NbtOps.INSTANCE, chunkPosMap).result().orElse(new CompoundTag());
        //pCompoundTag.put("biome_chunks", biomeChunks);
        ListTag biomeData = new ListTag();
        for (Map.Entry<UUID, BiomeData> entry : dataMap.entrySet()) {
            CompoundTag dataEntry = (CompoundTag) BiomeData.BIOME_DATA_CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue()).result().orElse(new CompoundTag());
            biomeData.add(dataEntry);
        }

        pCompoundTag.put("biome_data", biomeData);
        return pCompoundTag;
    }
}

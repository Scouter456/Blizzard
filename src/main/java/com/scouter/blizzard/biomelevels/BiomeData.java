package com.scouter.blizzard.biomelevels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.blizzard.events.BlizzardData;
import com.scouter.blizzard.util.CodecUtils;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class BiomeData {

    public static Codec<BiomeData> BIOME_DATA_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                        ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(BiomeData::getBiomeResourceKey),
                        Codec.INT.fieldOf("level").forGetter(BiomeData::getLevel),
                        UUIDUtil.CODEC.fieldOf("id").forGetter(BiomeData::getUuid),
                        Codec.LONG_STREAM.fieldOf("longs").forGetter(BiomeData::getLongStream))
                .apply(builder, BiomeData::new);
    });

    private int level;
    private ResourceKey<Biome> biomeResourceKey;
    private UUID uuid;
    private LongOpenHashSet chunkLongs = new LongOpenHashSet();

    public BiomeData(ResourceKey<Biome> biomeResourceKey, int level, UUID uuid, LongStream stream) {
        this.biomeResourceKey = biomeResourceKey;
        this.level = level;
        this.uuid = uuid;
        List<Long> longList = stream.boxed().toList();
        this.chunkLongs.addAll(longList);
    }

    public BiomeData(ResourceKey<Biome> biomeResourceKey, int level, UUID uuid) {
        this.biomeResourceKey = biomeResourceKey;
        this.level = level;
        this.uuid = uuid;
    }
    public BiomeData(ResourceKey<Biome> biomeResourceKey, int level) {
        this.biomeResourceKey = biomeResourceKey;
        this.level = level;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getLevel() {
        return level;
    }

    public void addLevel() {
        this.level++;
    }

    public void averageLevel(int level) {
        this.level = Mth.ceil((this.level + level) / 2.0);
    }


    public void averageLevelByChunks(int chunks) {
        this.level = Mth.ceil((double) this.level / chunks);
    }


    public ResourceKey<Biome> getBiomeResourceKey() {
        return biomeResourceKey;
    }

    public boolean sameBiome(ResourceKey<Biome> biomeResourceKey) {
        return this.biomeResourceKey.equals(biomeResourceKey);
    }


    public void add(ChunkPos pos){
        this.chunkLongs.add(pos.toLong());
    }

    public void add(long chunkLong){
        this.chunkLongs.add(chunkLong);
    }

    public void addAllC(Collection<ChunkPos> pos){
        for(ChunkPos pos1 : pos) {
            this.chunkLongs.add(pos1.toLong());
        }
    }

    public void addAllL(Collection<Long> chunkLong){
        this.chunkLongs.addAll(chunkLong);
    }

    public boolean hasChunk(ChunkPos pos) {
        return chunkLongs.contains(pos.toLong());
    }

    public boolean hasChunk(long chunkLong) {
        return chunkLongs.contains(chunkLong);
    }


    public Collection<Long> getChunks() {
        return new ArrayList<>(chunkLongs);
    }

    public LongStream getLongStream() {
        return this.chunkLongs.longStream();
    }
}

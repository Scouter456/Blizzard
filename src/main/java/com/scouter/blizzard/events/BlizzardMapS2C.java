package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.scouter.blizzard.util.CodecUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class BlizzardMapS2C {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Codec<Map<ChunkPos, BlizzardData>> BLIZZARD_MAPPER = Codec.unboundedMap(CodecUtils.CHUNK_POS_CODEC_STRING, BlizzardData.CODEC)
            .xmap(Map::copyOf, Map::copyOf)
            .orElse(e -> {
                        LOGGER.error("Failed to parse Blizzard Entries can't send packet! Due to " + e);
                    },
                    new ConcurrentHashMap<>());


    protected static Map<ChunkPos, BlizzardData> SYNCED_DATA = new ConcurrentHashMap<>();
    private final Map<ChunkPos, BlizzardData> map;
    private final ResourceKey<Level> levelResourceKey;

    public BlizzardMapS2C(Map<ChunkPos, BlizzardData> map, ResourceKey<Level> levelResourceKey) {
        this.map = map;
        this.levelResourceKey = levelResourceKey;
    }

    public void encode(FriendlyByteBuf buffer) {
        //AtomicReference<CompoundTag> compoundTag = new AtomicReference<>();
        //BLIZZARD_MAPPER.encodeStart(NbtOps.INSTANCE, this.map).get()
        //        .ifLeft(r -> {
        //            compoundTag.set((CompoundTag) r);
        //        }).ifRight(partial -> LOGGER.error("Failed to serialize blizzardData, Because of {}", partial.message()));
        buffer.writeWithCodec(NbtOps.INSTANCE, BLIZZARD_MAPPER, this.map);
        //ResourceLocation resourceLocation = this.levelResourceKey.registry();
        //buffer.writeWithCodec(NbtOps.INSTANCE, Codec.STRING, resourceLocation.toString());
        //buffer.writeNbt(compoundTag.get());
        StringTag encodedTag = (StringTag) (Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.levelResourceKey).result().orElse(new CompoundTag()));
        //ResourceLocation resourceLocation = this.levelResourceKey.registry();
        //buf.writeWithCodec(NbtOps.INSTANCE, Codec.STRING, resourceLocation.toString());
        CompoundTag tag = new CompoundTag();
        tag.put("level", encodedTag);
        buffer.writeNbt(tag);
    }

    public static BlizzardMapS2C decode(FriendlyByteBuf buffer) {
        Map<ChunkPos, BlizzardData> decodedMap = buffer.readWithCodec(NbtOps.INSTANCE, BLIZZARD_MAPPER);
        //String rl = buffer.readWithCodec(NbtOps.INSTANCE, Codec.STRING);
        //ResourceKey<Level> decodedLevel = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(rl));
        ResourceKey<Level> decodedLevel =  Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, buffer.readNbt().get("level")).result().orElse(null);
        //CompoundTag receivedTag = buffer.readNbt();
        //Map<ChunkPos, BlizzardData> decodedMap = BLIZZARD_MAPPER.parse(NbtOps.INSTANCE, receivedTag).result().orElse(new ConcurrentHashMap<>());

        return new BlizzardMapS2C(decodedMap, decodedLevel);
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        SYNCED_DATA = this.map;
        Map<ChunkPos, BlizzardData> dataMap = new ConcurrentHashMap<>(SYNCED_DATA);
        BlizzardClientData.setData(this.levelResourceKey, dataMap);
    }
}

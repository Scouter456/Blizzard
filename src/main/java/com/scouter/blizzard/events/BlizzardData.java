package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.blizzard.util.CodecUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public class BlizzardData {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<BlizzardData> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                Codec.INT.fieldOf("blizzard_strength").forGetter(BlizzardData::getBlizzardStrength),
                Codec.BOOL.fieldOf("has_blizzard").forGetter(BlizzardData::isHasBlizzard),
                        CodecUtils.CHUNK_POS_CODEC.fieldOf("chunk").forGetter(BlizzardData::getChunkPos))
                .apply(builder, BlizzardData::new);
    });
    private int blizzardStrength = 0;
    private boolean hasBlizzard;
    private ChunkPos chunkPos;
    public BlizzardData(int blizzardStrength, boolean hasBlizzard, ChunkPos chunkPos){
        this.hasBlizzard = hasBlizzard;
        this.blizzardStrength = blizzardStrength;
        this.chunkPos = chunkPos;
    }

    public BlizzardData() {

    }

    public int getBlizzardStrength() {
        return blizzardStrength;
    }

    public void setBlizzardStrength(int blizzardStrength) {
        this.blizzardStrength = blizzardStrength;
    }

    public void setHasBlizzard(boolean hasBlizzard) {
        this.hasBlizzard = hasBlizzard;
    }
    public ChunkPos getChunkPos(){
        return chunkPos;
    }
    public boolean isHasBlizzard() {
        return hasBlizzard;
    }

    public CompoundTag serialize(BlizzardData data) {
         AtomicReference<CompoundTag> compoundTag = new AtomicReference<>();
         DataResult<Tag> questResult = BlizzardData.CODEC.encodeStart(NbtOps.INSTANCE, data);
         questResult.get()
                 .ifLeft(result -> {
                     compoundTag.set((CompoundTag) result);
                 })
                 .ifRight(partial -> LOGGER.error("Failed to serialize blizzardData at {}", this.getChunkPos()));
         return compoundTag.get();
    }


    public static BlizzardData deserialize(CompoundTag tag) {
        AtomicReference<BlizzardData> data = new AtomicReference<>();
        BlizzardData.CODEC.decode(NbtOps.INSTANCE, tag)
                .get()
                .ifLeft(result -> {
                    data.set(result.getFirst());

                })
                .ifRight(partial -> LOGGER.error("Failed to parse data nbt for {} due to: {}", tag, partial.message()));
        return data.get();
    }

    public static BlizzardData defaultData(ChunkPos pos){
        BlizzardData data = new BlizzardData(0 ,false, pos);
        return data;
    }
}

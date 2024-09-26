package com.scouter.blizzard.biomelevels;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

public class ChunkMap {
    private LongOpenHashSet chunkLongs = new LongOpenHashSet();
    private ResourceKey<Biome> biomeResourceKey;

    public ChunkMap(ResourceKey<Biome> biomeResourceKey) {
        this.biomeResourceKey = biomeResourceKey;
    }

    public void add(ChunkPos pos){
        this.chunkLongs.add(pos.toLong());
    }

    public void add(long chunkLong){
        this.chunkLongs.add(chunkLong);
    }

    public boolean hasChunk(ChunkPos pos) {
        return chunkLongs.contains(pos.toLong());
    }

    public boolean hasChunk(long chunkLong) {
        return chunkLongs.contains(chunkLong);
    }

    public ResourceKey<Biome> getBiomeResourceKey() {
        return biomeResourceKey;
    }

    public boolean sameBiome(ResourceKey<Biome> biomeResourceKey) {
        return this.biomeResourceKey.equals(biomeResourceKey);
    }
}

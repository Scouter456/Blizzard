package com.scouter.blizzard.events;

import net.minecraft.world.level.ChunkPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlizzardClientData
{
    private static Map<ChunkPos, BlizzardData> blizzardData = new ConcurrentHashMap<>();

    public static BlizzardData getBlizzardClientData(ChunkPos chunkPos){
        return blizzardData.computeIfAbsent(chunkPos, (d) -> BlizzardData.defaultData());
    }

    public static void setData(ChunkPos pos, BlizzardData data){
        blizzardData.put(pos, data);
    }
}

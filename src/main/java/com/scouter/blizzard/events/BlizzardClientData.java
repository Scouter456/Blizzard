package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlizzardClientData
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Map<ChunkPos, BlizzardData> blizzardOverworldData = new ConcurrentHashMap<>();
    private static Map<ChunkPos, BlizzardData>  blizzardNetherData = new ConcurrentHashMap<>();
    private static Map<ChunkPos, BlizzardData> blizzardEndData =new ConcurrentHashMap<>();
    public static BlizzardData getBlizzardClientData(ResourceKey<Level> level, ChunkPos chunkPos){
        if(level == Level.OVERWORLD) {
            return blizzardOverworldData.computeIfAbsent(chunkPos, (d) -> BlizzardData.defaultData(chunkPos));
        }
        if(level == Level.END) {
            return blizzardEndData.computeIfAbsent(chunkPos, (d) -> BlizzardData.defaultData(chunkPos));
        }
        return blizzardNetherData.computeIfAbsent(chunkPos, (d) -> BlizzardData.defaultData(chunkPos));
    }

    public static void setData(ResourceKey<Level> level, ChunkPos pos, BlizzardData data){
        if(level == Level.OVERWORLD) {
            blizzardOverworldData.put(pos, data);
        }
        if(level == Level.END) {
            blizzardEndData.put(pos, data);
        }
        blizzardNetherData.put(pos, data);
    }

    public static void setData(ResourceKey<Level> level, Map<ChunkPos, BlizzardData> map){
        if(level == Level.OVERWORLD) {
            blizzardOverworldData = map;
        }
        if(level == Level.END) {
            blizzardEndData = map;
        }
        blizzardNetherData = map;
    }

}

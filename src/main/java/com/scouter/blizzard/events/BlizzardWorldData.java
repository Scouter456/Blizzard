package com.scouter.blizzard.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlizzardWorldData extends SavedData {
    private Map<ChunkPos, BlizzardData> blizzardData = new ConcurrentHashMap<>();
    public static BlizzardWorldData get(Level level){
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        // Get the vanilla storage manager from the level
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        // Get the blizzardData if it already exists. Otherwise, create a new one.
        return storage.computeIfAbsent(BlizzardWorldData::new, BlizzardWorldData::new, "blizzardData");
    }

    public void setBlizzardData(ChunkPos pos, BlizzardData data) {
        blizzardData.put(pos, data);
        setDirty();
    }

    public BlizzardWorldData(){
    }

    public BlizzardWorldData(CompoundTag nbt) {
        // Load player data from the provided CompoundTag
        ListTag blizzardList = nbt.getList("blizzardMap", 10);
        for (int i = 0; i < blizzardList.size(); i++) {
            CompoundTag blizzardEntry = blizzardList.getCompound(i);
            int x = blizzardEntry.getInt("chunkX");
            int z = blizzardEntry.getInt("chunkZ");
            ChunkPos chunkPos = new ChunkPos(x,z);
            BlizzardData blizzardData1 = BlizzardData.deserialize(blizzardEntry.getCompound("blizzardData"));
            blizzardData.put(chunkPos, blizzardData1);

            BMessages.sendMSGToAll(new BlizzardS2C(chunkPos, blizzardData1));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        // Save player data to the provided CompoundTag
        ListTag blizzardList = new ListTag();
        for (Map.Entry<ChunkPos, BlizzardData> entry : blizzardData.entrySet()) {
            CompoundTag blizzardEntry = new CompoundTag();
            blizzardEntry.putInt("chunkX", entry.getKey().x);
            blizzardEntry.putInt("chunkXZ", entry.getKey().z);
            blizzardEntry.put("blizzardData", entry.getValue().serialize());
            BMessages.sendMSGToAll(new BlizzardS2C(entry.getKey(), entry.getValue()));
        }



        nbt.put("blizzardMap", blizzardList);
        return nbt;
    }
}

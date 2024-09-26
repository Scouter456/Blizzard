package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlizzardWorldData extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();
    private Map<ChunkPos, BlizzardData> blizzardData = new ConcurrentHashMap<>();
    private ServerLevel level;
    public static BlizzardWorldData get(Level level){
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        // Get the vanilla storage manager from the level
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        BlizzardWorldData data = storage.computeIfAbsent(BlizzardWorldData::new, BlizzardWorldData::new, "blizzardData");
        // Get the blizzardData if it already exists. Otherwise, create a new one.
        data.setDirty();
        data.setLevel((ServerLevel) level);
        return data;
    }

    public void setBlizzardData(ChunkPos pos, BlizzardData data) {
        blizzardData.put(pos, data);
        BMessages.sendMSGToAll(new BlizzardS2C(pos, data, getLevel().dimension()));
        setDirty();
    }

    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public Map<ChunkPos, BlizzardData> getBlizzardData() {
        return blizzardData;
    }
    public boolean hasBlizzard(ChunkPos pos) {
        return blizzardData.containsKey(pos);
    }
    public BlizzardData getBlizzardData(ChunkPos pos) {
        if(blizzardData.containsKey(pos)) {
            return blizzardData.get(pos);
        }
        LOGGER.error("Trying to get blizzard data for chunk {} that doesnt exist ", pos);
        return null;
    }

    public void clearBlizzards() {
        blizzardData.clear();
        BMessages.sendMSGToAll(new BlizzardMapS2C(blizzardData, getLevel().dimension()));
    }

    public void clearBlizzardAtPos(ChunkPos pos) {
        blizzardData.remove(pos);
        BMessages.sendMSGToAll(new BlizzardMapS2C(blizzardData, getLevel().dimension()));
    }
    public BlizzardWorldData(){
    }

    public BlizzardWorldData(CompoundTag nbt) {

        ListTag blizzardList = nbt.getList("blizzardMap", 10);
        for (int i = 0; i < blizzardList.size(); i++) {
            CompoundTag blizzardEntry = blizzardList.getCompound(i);
            BlizzardData blizzardData1 = BlizzardData.deserialize(blizzardEntry);
            blizzardData.put(blizzardData1.getChunkPos(), blizzardData1);
        }

        //BMessages.sendMSGToAll(new BlizzardMapS2C(blizzardData));
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        // Save player data to the provided CompoundTag
        //BMessages.sendMSGToAll(new BlizzardMapS2C(blizzardData));
        ListTag blizzardList = new ListTag();
        for (Map.Entry<ChunkPos, BlizzardData> entry : blizzardData.entrySet()) {
            CompoundTag blizzardEntry = entry.getValue().serialize(entry.getValue());
            blizzardList.add(blizzardEntry);
        }

        nbt.put("blizzardMap", blizzardList);
        return nbt;
    }
}

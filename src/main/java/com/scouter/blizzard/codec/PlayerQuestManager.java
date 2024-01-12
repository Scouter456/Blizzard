package com.scouter.blizzard.codec;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerQuestManager extends SavedData {

    private Map<UUID, Set<Quests>> playerQuests = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static PlayerQuestManager get(Level level, UUID uuid){
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        // Get the vanilla storage manager from the level
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        // Get the PlayerQuests if it already exists. Otherwise, create a new one.
        return storage.computeIfAbsent(PlayerQuestManager::new, PlayerQuestManager::new, uuid.toString());
    }

    public Set<Quests> getQuestsForUUID(UUID uuid) {
        return playerQuests.computeIfAbsent(uuid, s -> new HashSet<>());
    }

    public void addQuestToUUID(UUID uuid, Quests quests) {
        Set<Quests> questsSet = getQuestsForUUID(uuid);
        questsSet.add(quests);
        playerQuests.put(uuid, questsSet);
        setDirty();
    }

    public Map<UUID, Set<Quests>> getPlayerQuests() {
        return playerQuests;
    }

    public PlayerQuestManager(){
    }

    public PlayerQuestManager(CompoundTag nbt) {
        // Load player data from the provided CompoundTag
        ListTag playerQuest = nbt.getList("playerQuests", 10);
        for (int i = 0; i < playerQuest.size(); i++) {
            CompoundTag entry = playerQuest.getCompound(i);
            UUID playerUUID = entry.getUUID("playerUUID");
            ListTag quests = entry.getList("quests", 10);
            Set<Quests> questsSet = new HashSet<>();
            for(int j = 0; j < quests.size(); j++) {
                CompoundTag questEntry = quests.getCompound(j);
                Quests.SERIALIZER_CODEC.decode(NbtOps.INSTANCE, questEntry)
                        .get()
                        .ifLeft(result -> {
                            Quests questRes = result.getFirst();
                            questsSet.add(questRes);
                        })
                        .ifRight(partial -> LOGGER.error("Failed to load saveData for player with UUID {} due to: {}", playerUUID, partial.message()));
            }
            playerQuests.put(playerUUID, questsSet);
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {

        ListTag questList = new ListTag();
        for (Map.Entry<UUID, Set<Quests>> entry : playerQuests.entrySet()) {
            CompoundTag questEntry = new CompoundTag();
            questEntry.putUUID("playerUUID", entry.getKey());
            Set<Quests> questsSet = entry.getValue();
            ListTag questsTag = new ListTag();
            for(Quests  quests : questsSet) {
                DataResult<Tag> compoundTag = Quests.SERIALIZER_CODEC.encodeStart(NbtOps.INSTANCE, quests);
                compoundTag.get()
                        .ifLeft(result -> {
                            questsTag.add(result.copy());
                        })
                        .ifRight(partial -> LOGGER.error("Failed to save quest {} for player with UUID {}", entry.getKey(), quests));
            }
            questEntry.put("quests", questsTag);
            questList.add(questEntry);
        }
        nbt.put("playerQuests", questList);
        return nbt;
    }
}

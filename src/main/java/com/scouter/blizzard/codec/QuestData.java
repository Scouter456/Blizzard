package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.UUID;

public class QuestData {
    public static Codec<QuestData> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    UUIDUtil.CODEC.fieldOf("questId").forGetter(s -> s.questID),
                    UUIDUtil.CODEC.fieldOf("giverId").forGetter(s -> s.giverID),
                    UUIDUtil.CODEC.fieldOf("turnInId").forGetter(s -> s.turnInID),
                    Codec.LONG.fieldOf("startTime").forGetter(s -> s.startTime),
                    Codec.LONG.fieldOf("completeTime").forGetter(s -> s.startTime),
                    Codec.BOOL.fieldOf("completed").forGetter(s -> s.completed)
                    )
            .apply(inst, QuestData::new)
    );
    private UUID questID;
    private UUID giverID;
    private UUID turnInID;
    private long startTime;
    private long completeTime;
    private boolean completed;

    public QuestData(UUID questID, UUID giverID, UUID turnInID, long startTime, long completeTime, boolean completed) {
        this.questID =questID;
        this.giverID = giverID;
        this.turnInID = turnInID;
        this.startTime = startTime;
        this.completeTime = completeTime;
        this.completed = completed;
    }


    public UUID getGiverID() {
        return giverID;
    }

    public UUID getQuestID() {
        return questID;
    }

    public UUID getTurnInID() {
        return turnInID;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isCompleted() {
        boolean isCompleted = (getCompleteTime() -  getStartTime()) >= 0;
        setCompleted(isCompleted);
        return isCompleted;
    }

    public void setGiverID(UUID giverID) {
        this.giverID = giverID;
    }

    public void setQuestID(UUID questID) {
        this.questID = questID;
    }

    public void setTurnInID(UUID turnInID) {
        this.turnInID = turnInID;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static QuestData createEmpty() {
        UUID randomUUID = UUID.randomUUID();
        return new QuestData(randomUUID,randomUUID,randomUUID,0,0,false);
    }
}

package com.scouter.blizzard.codec.builders;

import com.scouter.blizzard.codec.Quests;
import com.scouter.blizzard.codec.Reward;
import com.scouter.blizzard.codec.Task;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class QuestsBuilder {
    private String questName;
    private final List<Task> taskList = new ArrayList<>();
    private ResourceLocation advancementQuest = new ResourceLocation("");
    private ResourceLocation resourceLocation = new ResourceLocation("");
    private final List<Reward.QuestRewards> questRewards = new ArrayList<>();
    private boolean isRootQuest = false;
    private EntityType<?> attachEntity;

    public static QuestsBuilder Builder(){
        return new QuestsBuilder();
    }
    public QuestsBuilder withResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }
    public QuestsBuilder withQuestName(String questName) {
        this.questName = questName;
        return this;
    }

    public QuestsBuilder withTask(Task task) {
        this.taskList.add(task);
        return this;
    }

    public QuestsBuilder withAdvancementQuest(ResourceLocation advancementQuest) {
        this.advancementQuest = advancementQuest;
        return this;
    }

    public QuestsBuilder withReward(Reward.QuestRewards reward) {
        this.questRewards.add(reward);
        return this;
    }

    public QuestsBuilder asRootQuest(boolean isRootQuest) {
        this.isRootQuest = isRootQuest;
        return this;
    }

    public QuestsBuilder withAttachEntity(EntityType<?> attachEntity) {
        this.attachEntity = attachEntity;
        return this;
    }

    public Quests build() {
        Quests quests = new Quests(questName, taskList, advancementQuest, questRewards, isRootQuest, attachEntity);
        quests.setQuestResourceLocation(resourceLocation);
        return quests;
    }
}


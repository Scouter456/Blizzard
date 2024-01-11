package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;


public class Quests {

    public static Codec<Quests> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Task.DIRECT_CODEC.listOf().fieldOf("tasks").forGetter(s -> s.taskList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest)
            )
            .apply(inst, Quests::new)
    );

    public static Codec<Quests> SERIALIZER_CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Task.DIRECT_SERIALIZER_CODEC.listOf().fieldOf("tasks").forGetter(s -> s.taskList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest)
            )
            .apply(inst, Quests::new)
    );

    private String questName;
    private List<Task> taskList;
    private ResourceLocation advancementQuest;

    public Quests(String questName, List<Task> taskList, ResourceLocation advancementQuest){
        this.questName = questName;
        this.taskList = taskList;
        this.advancementQuest = advancementQuest;
    }

    public String getQuestName() {
        return questName;
    }

    public List<Task> getTasks() {
        return taskList;
    }

    public ResourceLocation getAdvancementQuest() {
        return advancementQuest;
    }
}

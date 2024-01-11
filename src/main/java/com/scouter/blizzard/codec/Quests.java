package com.scouter.blizzard.codec;

import com.mojang.math.Constants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Quests {

    public static Codec<Quests> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Quest.DIRECT_CODEC.listOf().fieldOf("quests").forGetter(s -> s.questList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest)
            )
            .apply(inst, Quests::new)
    );

    private String questName;
    private List<Quest> questList;
    private ResourceLocation advancementQuest;

    public Quests(String questName, List<Quest> questList, ResourceLocation advancementQuest){
        this.questName = questName;
        this.questList = questList;
        this.advancementQuest = advancementQuest;
    }

    public String getQuestName() {
        return questName;
    }

    public List<Quest> getQuests() {
        return questList;
    }

    public ResourceLocation getAdvancementQuest() {
        return advancementQuest;
    }

    public void setData(CompoundTag compoundTag) {
        ListTag questTag = compoundTag.getList("questData", Tag.TAG_COMPOUND);
        for (int i = 0; i < questTag.size(); i++) {
            CompoundTag tag = questTag.getCompound(i);
            if (i < getQuests().size()) {
                getQuests().get(i).setData(tag);
            }
        }
    }

    public CompoundTag serializeData() {
        CompoundTag compoundTag = new CompoundTag();
        ListTag questTag = new ListTag();
        for (Quest quest : getQuests()) {
            CompoundTag tag = quest.getSerializer();
            questTag.add(tag);
        }
        compoundTag.put("questData", questTag);
        return compoundTag;
    }

}

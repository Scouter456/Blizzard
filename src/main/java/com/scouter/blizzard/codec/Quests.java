package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


public class Quests {

    public static Codec<Quests> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Task.DIRECT_CODEC.listOf().fieldOf("tasks").forGetter(s -> s.taskList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest),
                    Reward.QuestRewards.CODEC.listOf().optionalFieldOf("rewards", Collections.emptyList()).forGetter(s -> s.questRewards)
            )
            .apply(inst, Quests::new)
    );
    //TODO add quest giver mob resource location e.g. ResourceLocation resourcelocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity); default to minecraft villager
    //TODO add questRewards
    public static Codec<Quests> SERIALIZER_CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Task.DIRECT_SERIALIZER_CODEC.listOf().fieldOf("tasks").forGetter(s -> s.taskList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest),
                    Reward.QuestRewards.CODEC.listOf().optionalFieldOf("rewards", Collections.emptyList()).forGetter(s -> s.questRewards),
                    QuestData.CODEC.fieldOf("quest_data").forGetter(s -> s.questData)
            )
            .apply(inst, Quests::new)
    );

    private String questName;
    private List<Task> taskList;
    private ResourceLocation advancementQuest;
    private List<Reward.QuestRewards> questRewards;
    private QuestData questData;

    public Quests(String questName, List<Task> taskList, ResourceLocation advancementQuest, List<Reward.QuestRewards> questRewards){
        this.questName = questName;
        this.taskList = taskList;
        this.advancementQuest = advancementQuest;
        this.questRewards = questRewards;
    }


    public Quests(String questName, List<Task> taskList, ResourceLocation advancementQuest, List<Reward.QuestRewards> questRewards, QuestData questData){
        this.questName = questName;
        this.taskList = taskList;
        this.advancementQuest = advancementQuest;
        this.questRewards = questRewards;
        this.questData = questData;
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

    public List<Reward.QuestRewards> getQuestRewards() {
        return questRewards;
    }

    public QuestData getQuestData() {
        return questData;
    }

    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }


    //TODO improve messaging
    public void giveRewards(ServerLevel level, Player player) {
        List<Component> components = Component.translatable("rewards.quest_rewards").withStyle(ChatFormatting.ITALIC).toFlatList();

        for(Reward.QuestRewards reward :questRewards) {
            reward.experienceReward().generateExperience(level, this,  player);
            reward.experienceReward().appendHoverText(c ->
            {
                components.add(c);
            });
            reward.itemRewards().generateLoot(level, this,  player,  e -> spawnCompletionItem(e, level, player));
            reward.itemRewards().appendHoverText(c ->
            {
                components.add(c);
            });
        }

        for(Component component : components) {
            player.sendSystemMessage(component);
        }

    }


    public void spawnCompletionItem(ItemStack stack, ServerLevel level, Player player) {
        RandomSource random = player.getRandom();
        ItemEntity i = new ItemEntity(level, 0, 0, 0, stack);
        i.setPos(player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ());
        i.setDeltaMovement(Mth.nextDouble(random, -0.1, 0.1), player.getBbHeight() / 20F, Mth.nextDouble(random, -0.1, 0.1));
        i.setUnlimitedLifetime();
        level.addFreshEntity(i);
    }


    public Quests copyQuest() {
        return new Quests(questName, taskList,advancementQuest, questRewards);
    }

    public Quests startQuest(ServerLevel serverLevel, UUID giverID, UUID playerID) {
        Quests quests = this.copyQuest();
        UUID questId = UUID.randomUUID();
        long currentTime = serverLevel.getGameTime();
        QuestData data = new QuestData(questId, giverID, playerID, currentTime, 0, false);
        quests.setQuestData(data);
        return quests;
    }

    public boolean isCompleted() {
        if(this.questData != null) {
            return this.questData.isCompleted();
        }
        return false;
    }

    public UUID getQuestID() {
        if(questData != null) {
            return questData.getQuestID();
        }
        return null;
    }
}

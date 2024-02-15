package com.scouter.blizzard.codec;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Represents a quest with tasks, rewards, and associated data.
 */
public class Quests {
    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * Codec for serialization and deserialization of Quests.
     */
    public static Codec<Quests> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("quest_name").forGetter(s -> s.questName),
                    Task.DIRECT_CODEC.listOf().fieldOf("tasks").forGetter(s -> s.taskList),
                    ResourceLocation.CODEC.optionalFieldOf("advancement_quest", new ResourceLocation("")).forGetter(s -> s.advancementQuest),
                    Reward.QuestRewards.CODEC.listOf().optionalFieldOf("rewards", Collections.emptyList()).forGetter(s -> s.questRewards),
                    Codec.BOOL.fieldOf("is_root_quest").forGetter(s -> s.isRootQuest),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("attach_entity").forGetter(s -> s.attachEntity)
            )
            .apply(inst, Quests::new)
    );

    /**
     * Serializer Codec for serialization and deserialization of Quests with QuestData.
     */
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
    private Map<TaskType, List<Task>> taskTypeMap;
    private ResourceLocation advancementQuest;
    private ResourceLocation resourceLocation;
    private List<Reward.QuestRewards> questRewards;
    private QuestData questData;
    private boolean isRootQuest;
    private EntityType<?> attachEntity;

    /**
     * Constructor for Quests.
     *
     * @param questName        The name of the quest.
     * @param taskList         List of tasks associated with the quest.
     * @param advancementQuest The optional advancement associated with the quest.
     * @param questRewards     List of quest rewards.
     * @param isRootQuest      Indicates if the quest is a root quest.
     * @param attachEntity     The optional entity type attached to the quest.
     */
    public Quests(String questName, List<Task> taskList, ResourceLocation advancementQuest, List<Reward.QuestRewards> questRewards, boolean isRootQuest, EntityType<?> attachEntity) {
        this.questName = questName;
        this.taskList = taskList;
        this.taskTypeMap = computeTasks();
        this.advancementQuest = advancementQuest;
        this.questRewards = questRewards;
        this.isRootQuest = isRootQuest;
        this.attachEntity = attachEntity;
    }

    /**
     * Constructor for Quests with QuestData.
     *
     * @param questName        The name of the quest.
     * @param taskList         List of tasks associated with the quest.
     * @param advancementQuest The optional advancement associated with the quest.
     * @param questRewards     List of quest rewards.
     * @param questData        The quest data associated with the quest.
     */
    public Quests(String questName, List<Task> taskList, ResourceLocation advancementQuest, List<Reward.QuestRewards> questRewards, QuestData questData) {
        this.questName = questName;
        this.taskList = taskList;
        this.taskTypeMap = computeTasks();
        this.advancementQuest = advancementQuest;
        this.questRewards = questRewards;
        this.questData = questData;
    }

    /**
     * Gets the name of the quest.
     *
     * @return The quest name.
     */
    public String getQuestName() {
        return questName;
    }

    /**
     * Gets the list of tasks associated with the quest.
     *
     * @return List of tasks.
     */
    public List<Task> getTasks() {
        return taskList;
    }

    /**
     * Gets a map of tasks grouped by task type.
     *
     * @return Map of task types to tasks.
     */
    public Map<TaskType, List<Task>> getTaskTypeMap() {
        return taskTypeMap;
    }

    //todo maybe compute again?
    /**
     * Gets a list of tasks for a specific task type.
     *
     * @param taskType The task type to retrieve tasks for.
     * @return List of tasks for the specified task type.
     */
    public List<Task> getTaskForType(TaskType taskType) {
        return taskTypeMap.computeIfAbsent(taskType, (e) -> computeTasks().get(taskType));
    }

    /**
     * Gets the optional advancement associated with the quest.
     *
     * @return The advancement quest.
     */
    public ResourceLocation getAdvancementQuest() {
        return advancementQuest;
    }

    /**
     * Gets the list of quest rewards.
     *
     * @return List of quest rewards.
     */
    public List<Reward.QuestRewards> getQuestRewards() {
        return questRewards;
    }

    /**
     * Gets the quest data associated with the quest.
     *
     * @return The quest data.
     */
    public QuestData getQuestData() {
        return questData;
    }

    /**
     * Sets the quest data for the quest.
     *
     * @param questData The quest data to set.
     */
    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    /**
     * Checks if the quest is a root quest.
     *
     * @return True if the quest is a root quest, false otherwise.
     */
    public boolean isRootQuest() {
        return isRootQuest;
    }

    /**
     * Gets the optional entity type attached to the quest.
     *
     * @return The attached entity type.
     */
    public EntityType<?> getAttachEntity() {
        return attachEntity;
    }

    //TODO improve this as a whole and also messaging
    /**
     * Gives rewards to the player upon quest completion.
     *
     * @param level  The server level.
     * @param player The player to receive rewards.
     */
    public void giveRewards(ServerLevel level, Player player) {
        List<Component> components = Component.translatable("rewards.quest_rewards").withStyle(ChatFormatting.ITALIC).toFlatList();

        for (Reward.QuestRewards reward : questRewards) {
            reward.experienceReward().generateExperience(level, this, player);
            reward.experienceReward().appendHoverText(c ->
            {
                components.add(c);
            });
            reward.itemRewards().generateLoot(level, this, player, e -> spawnCompletionItem(e, level, player));
            reward.itemRewards().appendHoverText(c ->
            {
                components.add(c);
            });
        }

        for (Component component : components) {
            player.sendSystemMessage(component);
        }
    }

    /**
     * Spawns an item entity for the completion item.
     *
     * @param stack  The item stack to spawn.
     * @param level  The server level.
     * @param player The player for whom the item is spawned.
     */
    public void spawnCompletionItem(ItemStack stack, ServerLevel level, Player player) {
        RandomSource random = player.getRandom();
        ItemEntity i = new ItemEntity(level, 0, 0, 0, stack);
        i.setPos(player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ());
        i.setDeltaMovement(Mth.nextDouble(random, -0.1, 0.1), player.getBbHeight() / 20F, Mth.nextDouble(random, -0.1, 0.1));
        i.setUnlimitedLifetime();
        level.addFreshEntity(i);
    }

    /**
     * Creates a copy of the quest.
     *
     * @return The copied quest.
     */
    public Quests copyQuest() {
        return new Quests(questName, taskList, advancementQuest, questRewards, isRootQuest, attachEntity);
    }

    /**
     * Starts the quest for a player in the given server level.
     *
     * @param serverLevel The server level.
     * @param giverID     The UUID of the quest giver.
     * @param playerID    The UUID of the player starting the quest.
     * @return The started quest.
     */
    public Quests startQuest(ServerLevel serverLevel, UUID giverID, UUID playerID) {
        Quests quests = this.copyQuest();
        UUID questId = UUID.randomUUID();
        long currentTime = serverLevel.getGameTime();
        QuestData data = new QuestData(questId, giverID, playerID, currentTime, 0, false);
        quests.setQuestData(data);
        return quests;
    }

    /**
     * Checks if the quest is completed.
     *
     * @return True if the quest is completed, false otherwise.
     */
    public boolean isCompleted() {
        return questData != null && questData.isCompleted();
    }

    /**
     * Gets the UUID of the quest.
     *
     * @return The UUID of the quest, or null if questData is null.
     */
    public UUID getQuestID() {
        return questData != null ? questData.getQuestID() : null;
    }
    public void setQuestResourceLocation(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }
    public ResourceLocation getQuestResourceLocation() {
        return resourceLocation;
    }

    private Map<TaskType, List<Task>> computeTasks() {
        Map<TaskType, List<Task>> taskTypeListMap = new HashMap<>();
        for (Task task : taskList) {
            taskTypeListMap.computeIfAbsent(task.getTaskType(), e -> new ArrayList<>()).add(task);
        }
        this.taskTypeMap = taskTypeListMap;
        return taskTypeListMap;
    }

    public JsonElement serializeQuest() {
        AtomicReference<JsonElement> jsonElement = new AtomicReference<>();
        DataResult<JsonElement> questResult = Quests.CODEC.encodeStart(JsonOps.INSTANCE, this);
        questResult.get()
                .ifLeft(result -> {
                    jsonElement.set(result);
                })
                .ifRight(partial -> LOGGER.error("Failed to serialize quest with id: {}", this.resourceLocation));
        return jsonElement.get();
    }
}

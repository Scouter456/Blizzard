package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.codec.*;
import com.scouter.blizzard.message.QuestsS2C;
import com.scouter.blizzard.message.RootQuestsS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static int counter;
    @SubscribeEvent
    public static void createBlizzard(TickEvent.PlayerTickEvent event) {
        ChunkPos chunkPos = event.player.chunkPosition();
        BlizzardData clientData = BlizzardClientData.getBlizzardClientData(chunkPos);
       //LOGGER.info("data " + clientData.getBlizzardStrength());
       //LOGGER.info("data2 " + clientData.isHasBlizzard());
        if (!(event.player instanceof ServerPlayer serverPlayer) || event.phase == TickEvent.Phase.END) return;

        if(!serverPlayer.level().isClientSide && counter++ % 40 == 0){
            if(serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.DIAMOND_BLOCK)) {
                BlizzardWorldData blizzardWorldData = BlizzardWorldData.get((ServerLevel) serverPlayer.level());
                chunkPos = serverPlayer.chunkPosition();
                BlizzardData blizzardData = new BlizzardData(10, true);
                blizzardWorldData.setBlizzardData(chunkPos, blizzardData);
            }
            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(serverLevel, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(serverPlayer.getUUID());
            TaskData data = new TaskData(serverLevel, serverPlayer);
            for(Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.FIND_STRUCTURE);
                for(Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }

    }

    @SubscribeEvent
    public static void killTask(LivingDeathEvent event) {
        Entity source  = event.getSource().getEntity();
        if(source != null && !source.level().isClientSide() && source instanceof ServerPlayer player) {
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, player.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, player);
            data.setKilledEntity(event.getEntity());
            for(Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.KILL);
                for(Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void breakTask(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if(player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockState state = event.getState();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setMinedBlock(state);
            for(Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.BREAK);
                for(Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void collectItemTask(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getEntity();
        if(player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getStack();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setObtainedItem(stack);
            for(Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.COLLECT);
                for(Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void brewPotionTask(PlayerBrewedPotionEvent event) {
        Player player = event.getEntity();
        if(player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getStack();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setObtainedItem(stack);
            for(Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.BREW);
                for(Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event){
        event.addListener(new QuestManager());
    }

    @SubscribeEvent
    public static void openQuests(PlayerInteractEvent.EntityInteract event) {
        if(event.getLevel().isClientSide) return;
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        ServerLevel serverLevel = (ServerLevel) event.getLevel();
        String id = entity.getEncodeId();
        ResourceLocation resourceLocation = new ResourceLocation(id);
        //TODO first check if this entity didnt have quests assigned before so we can fetch the old quests only then can we assign new quests to the entity.
        Map<ResourceLocation, Quests> questsMap = QuestManager.getRootQuestsForEntity(resourceLocation);
        //TODO now assign quest or do something ^^

    }


    @SubscribeEvent
    public static void synchDataPack(OnDatapackSyncEvent event){
        ServerPlayer player = event.getPlayer();
        List<ServerPlayer> playerList = event.getPlayerList().getPlayers();
        Map<ResourceLocation, Map<ResourceLocation, Quests>> rootQuests = QuestManager.getRootQuests();
        Map<ResourceLocation, Quests> quests = QuestManager.getQuests();

        if(player != null){
            BMessages.sendToPlayer(new QuestsS2C(quests), player);
            BMessages.sendToPlayer(new RootQuestsS2C(rootQuests), player);
        }


        if(playerList != null && !playerList.isEmpty()){
            for(ServerPlayer player1 : playerList){
                BMessages.sendToPlayer(new QuestsS2C(quests), player1);
                BMessages.sendToPlayer(new RootQuestsS2C(rootQuests), player1);
            }
        }
    }
}


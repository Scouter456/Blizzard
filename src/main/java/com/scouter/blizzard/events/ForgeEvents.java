package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.codec.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void createBlizzard(TickEvent.PlayerTickEvent event) {
        ChunkPos chunkPos = event.player.chunkPosition();
        BlizzardData clientData = BlizzardClientData.getBlizzardClientData(chunkPos);
       //LOGGER.info("data " + clientData.getBlizzardStrength());
       //LOGGER.info("data2 " + clientData.isHasBlizzard());
        if (!(event.player instanceof ServerPlayer serverPlayer) || event.phase == TickEvent.Phase.END) return;

        if(!serverPlayer.level().isClientSide){
            if(serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.DIAMOND_BLOCK)) {
                BlizzardWorldData blizzardWorldData = BlizzardWorldData.get((ServerLevel) serverPlayer.level());
                chunkPos = serverPlayer.chunkPosition();
                BlizzardData blizzardData = new BlizzardData(10, true);
                blizzardWorldData.setBlizzardData(chunkPos, blizzardData);
            }
            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
            PlayerQuestManager playerQuests = PlayerQuestManager.get(serverLevel);
            if(!QuestManager.getQuests().isEmpty() && serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.PAPER)) {
                playerQuests.addQuestToUUID(serverPlayer.getUUID(), QuestManager.getQuests().values().stream().collect(Collectors.toList()).get(0));
            }
            Set<Quests> quests = playerQuests.getQuestsForUUID(serverPlayer.getUUID());
            for(Quests quests1 : quests) {
                LOGGER.info("Quest: " + quests1.getQuestName());
            }
        }

    }

    @SubscribeEvent
    public static void killQuest(LivingDeathEvent event) {
        Entity source  = event.getSource().getEntity();
        if(source != null && !source.level().isClientSide() && source instanceof ServerPlayer player) {
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level);
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            for(Quests questsI : questsSet) {
                List<Quest> questList = questsI.getQuests();
                for(Quest quest : questList) {
                    quest.playerKillEntity(event.getEntity(), player, level);
                }
            }
            playerQuestManager.setDirty();
        }


    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event){
        event.addListener(new QuestManager());
    }
}


package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void createBlizzard(TickEvent.PlayerTickEvent event) {
        ChunkPos chunkPos = event.player.chunkPosition();
        BlizzardData clientData = BlizzardClientData.getBlizzardClientData(chunkPos);
        LOGGER.info("data " + clientData.getBlizzardStrength());
        LOGGER.info("data2 " + clientData.isHasBlizzard());
        if (!(event.player instanceof ServerPlayer serverPlayer) || event.phase == TickEvent.Phase.END) return;

        if(!serverPlayer.level().isClientSide){
            if(serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.DIAMOND_BLOCK)) {
                BlizzardWorldData blizzardWorldData = BlizzardWorldData.get((ServerLevel) serverPlayer.level());
                chunkPos = serverPlayer.chunkPosition();
                BlizzardData blizzardData = new BlizzardData(10, true);
                blizzardWorldData.setBlizzardData(chunkPos, blizzardData);
            }
        }
    }
}


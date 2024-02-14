package com.scouter.blizzard.setup;


import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.entities.BEntities;
import com.scouter.blizzard.entities.FishLocations;
import com.scouter.blizzard.entities.model.FishModel;
import com.scouter.blizzard.entities.renderer.FishRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(FMLClientSetupEvent event) {
        EntityRenderers.register(BEntities.MOTOR_FISH.get(), e -> new FishRenderer<>(e, new FishModel<>(FishLocations.MOTOR_FISH)));
    }


    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {

    }
}


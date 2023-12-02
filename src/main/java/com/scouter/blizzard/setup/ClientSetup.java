package com.scouter.blizzard.setup;


import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
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

    }


    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {

    }
}


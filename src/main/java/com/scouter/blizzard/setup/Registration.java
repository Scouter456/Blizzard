package com.scouter.blizzard.setup;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.events.BMessages;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


public class Registration {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BMessages.register();
    }
}

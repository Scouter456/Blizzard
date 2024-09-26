package com.scouter.blizzard.setup;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.block.BBlocks;
import com.scouter.blizzard.codec.TaskRegistries;
import com.scouter.blizzard.codec.TaskRegistry;
import com.scouter.blizzard.entities.BEntities;
import com.scouter.blizzard.events.BMessages;
import com.scouter.blizzard.events.particles.BParticles;
import com.scouter.blizzard.events.sound.BSounds;
import com.scouter.blizzard.items.BItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


public class Registration {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BMessages.register();
        BBlocks.BLOCKS.register(bus);
        BItems.ITEMS.register(bus);
        BEntities.ENTITY_TYPES.register(bus);
        BSounds.SOUNDS.register(bus);
        BParticles.PARTICLE_TYPES.register(bus);
        TaskRegistries.TASK_QUEST_TYPE_SERIALIZER.register(bus);
        TaskRegistry.TASK_TYPE_SERIALIZER.register(bus);
    }
}

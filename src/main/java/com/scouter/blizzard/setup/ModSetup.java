package com.scouter.blizzard.setup;

import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.entities.BEntities;
import com.scouter.blizzard.entities.FishEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            //BBlockFamilies.registerFamilies();
        });
        //QMessages.register();
    }

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
    }


    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(BEntities.MOTOR_FISH.get(), FishEntity.createAttributes().build());
        event.put(BEntities.SCARLET_MINNOW.get(), FishEntity.createAttributes().build());
        event.put(BEntities.LAZULI_SCALE.get(), FishEntity.createAttributes().build());
        event.put(BEntities.EMERALDINE_MOLA.get(), FishEntity.createAttributes().build());
        event.put(BEntities.AUREOFIN.get(), FishEntity.createAttributes().build());
        event.put(BEntities.GLITTERING_FISH.get(), FishEntity.createAttributes().build());

    }


}

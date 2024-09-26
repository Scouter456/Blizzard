package com.scouter.blizzard.entities;

import com.scouter.blizzard.Blizzard;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.scouter.blizzard.Blizzard.prefix;

public class BEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Blizzard.MODID);


    public static final RegistryObject<EntityType<FishEntity>> MOTOR_FISH = ENTITY_TYPES.register("motor_fish",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("motor_fish").toString()));


    //TODO these ones group in a big school and are fast
    public static final RegistryObject<EntityType<FishEntity>> SCARLET_MINNOW = ENTITY_TYPES.register("scarlet_minnow",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("scarlet_minnow").toString()));

    public static final RegistryObject<EntityType<FishEntity>> LAZULI_SCALE = ENTITY_TYPES.register("lazuli_scale",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("lazuli_scale").toString()));

    public static final RegistryObject<EntityType<FishEntity>> GLITTERING_FISH = ENTITY_TYPES.register("glittering_fish",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("glittering_fish").toString()));

    public static final RegistryObject<EntityType<FishEntity>> EMERALDINE_MOLA = ENTITY_TYPES.register("emeraldine_mola",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("emeraldine_mola").toString()));


    public static final RegistryObject<EntityType<FishEntity>> AUREOFIN = ENTITY_TYPES.register("aureofin",
            () -> EntityType.Builder.of(FishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.7F, 0.7F).clientTrackingRange(4)
                    .build(prefix("aureofin").toString()));
}

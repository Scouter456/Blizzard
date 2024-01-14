package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.scouter.blizzard.Blizzard.MODID;


public class TaskRegistry {



    public static final DeferredRegister<Codec<? extends Task>> TASK_TYPE_SERIALIZER = DeferredRegister.create(TaskRegistries.Keys.TASK_TYPE_SERIALIZERS, MODID);

    public static final RegistryObject<Codec<KillTask>> KILL_TASK = TASK_TYPE_SERIALIZER.register("kill", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_to_kill").forGetter(s -> s.entity),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("killer").forGetter(s -> s.player),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toKill),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, KillTask::new))
    );

    public static final RegistryObject<Codec<KillTask>> KILL_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("kill_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_to_kill").forGetter(s -> s.entity),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("killer").forGetter(s -> s.player),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toKill),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("killed").forGetter(s -> s.killedEnties)
            ).apply(builder, KillTask::new))
    );

    public static final RegistryObject<Codec<BreakTask>> BREAK_TASK = TASK_TYPE_SERIALIZER.register("break", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to_break").forGetter(s -> s.block),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toBreak),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, BreakTask::new))
    );

    public static final RegistryObject<Codec<BreakTask>> BREAK_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("break_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to_break").forGetter(s -> s.block),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toBreak),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("broken").forGetter(s -> s.broken)
            ).apply(builder, BreakTask::new))
    );

    public static final RegistryObject<Codec<BrewPotionTask>> BREW_TASK = TASK_TYPE_SERIALIZER.register("brewing", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("to_brew").forGetter(s -> s.potion),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toBrew),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, BrewPotionTask::new))
    );

    public static final RegistryObject<Codec<BrewPotionTask>> BREW_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("brewing_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("to_brew").forGetter(s -> s.potion),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toBrew),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("brewed").forGetter(s -> s.brewed)
                    ).apply(builder, BrewPotionTask::new))
    );

    public static final RegistryObject<Codec<CollectItemTask>> COLLECT_TASK = TASK_TYPE_SERIALIZER.register("collect", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("to_collect").forGetter(s -> s.item),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toCollect),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, CollectItemTask::new))
    );

    public static final RegistryObject<Codec<CollectItemTask>> COLLECT_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("collect_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("to_collect").forGetter(s -> s.item),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toCollect),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("collected").forGetter(s -> s.collected)
            ).apply(builder, CollectItemTask::new))
    );

    public static final RegistryObject<Codec<FindStructureTask>> FIND_STRUCTURE_TASK = TASK_TYPE_SERIALIZER.register("find_structure", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    ResourceLocation.CODEC.fieldOf("to_find").forGetter(s -> s.structure),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toFind),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, FindStructureTask::new))
    );

    public static final RegistryObject<Codec<FindStructureTask>> FIND_STRUCTURE_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("find_structure_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    ResourceLocation.CODEC.fieldOf("to_find").forGetter(s -> s.structure),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toFind),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("found").forGetter(s -> s.found)
            ).apply(builder, FindStructureTask::new))
    );
}

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

    public static final RegistryObject<Codec<MineTask>> MINE_TASK = TASK_TYPE_SERIALIZER.register("mine", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to_mine").forGetter(s -> s.block),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toMine),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id)
            ).apply(builder, MineTask::new))
    );

    public static final RegistryObject<Codec<MineTask>> MINE_TASK_SERIALIZER = TASK_TYPE_SERIALIZER.register("mine_serializer", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to_mine").forGetter(s -> s.block),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toMine),
                    ResourceLocation.CODEC.fieldOf("identifier").forGetter(s -> s.id),
                    Codec.INT.fieldOf("mined").forGetter(s -> s.mined)
            ).apply(builder, MineTask::new))
    );
}

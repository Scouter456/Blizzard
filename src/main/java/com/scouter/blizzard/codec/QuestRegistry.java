package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.scouter.blizzard.Blizzard.MODID;


public class QuestRegistry {



    public static final DeferredRegister<Codec<? extends Quest>> QUEST_TYPE_SERIALIZER = DeferredRegister.create(QuestRegistries.Keys.QUEST_TYPE_SERIALIZERS, MODID);

    public static final RegistryObject<Codec<KillQuest>> KILL_QUEST = QUEST_TYPE_SERIALIZER.register("kill", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_to_kill").forGetter(s -> s.entity),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("killer").forGetter(s -> s.player),
                    Codec.INT.optionalFieldOf("amount", 1).forGetter(s -> s.toKill)
            ).apply(builder, KillQuest::new))
    );


}

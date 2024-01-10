package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

import static com.scouter.blizzard.Blizzard.prefix;


public class QuestRegistries {
    static { init(); }
    public static final DeferredRegister<Codec<? extends Quest>> DEFERRED_QUEST_TYPE_SERIALIZER = DeferredRegister.create(Keys.QUEST_TYPE_SERIALIZERS, Keys.QUEST_TYPE_SERIALIZERS.location().getNamespace());
    public static final Supplier<IForgeRegistry<Codec<? extends Quest>>> QUEST_TYPE_SERIALIZER = DEFERRED_QUEST_TYPE_SERIALIZER.makeRegistry(() -> new RegistryBuilder<Codec<? extends Quest>>().disableSaving());

    public static final class Keys {
        public static final ResourceKey<Registry<Codec<? extends Quest>>> QUEST_TYPE_SERIALIZERS = key(prefix("quest_type_serializer").toString());
        public static final ResourceKey<Registry<Quest>> QUEST_TYPE = key(prefix("quest_type").toString());
        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(new ResourceLocation(name));
        }
        private static void init() {}

    }

    private static void init()
    {
        Keys.init();
    }
}

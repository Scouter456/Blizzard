package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static com.scouter.blizzard.Blizzard.prefix;


public class TaskRegistries {
    static { init(); }
    public static final DeferredRegister<Codec<? extends Task>> TASK_QUEST_TYPE_SERIALIZER = DeferredRegister.create(Keys.TASK_TYPE_SERIALIZERS, Keys.TASK_TYPE_SERIALIZERS.location().getNamespace());
    public static final Supplier<IForgeRegistry<Codec<? extends Task>>> TASK_TYPE_SERIALIZER = TASK_QUEST_TYPE_SERIALIZER.makeRegistry(() -> new RegistryBuilder<Codec<? extends Task>>().disableSaving());

    public static final class Keys {
        public static final ResourceKey<Registry<Codec<? extends Task>>> TASK_TYPE_SERIALIZERS = key(prefix("task_type_serializer").toString());
        public static final ResourceKey<Registry<Task>> TASK_TYPE = key(prefix("task_type").toString());
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

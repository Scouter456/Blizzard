package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public interface QuestTypes {
    Codec<QuestTypes> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> QuestRegistries.QUEST_TYPE_SERIALIZER.get().getCodec()).dispatch(QuestTypes::codec, Function.identity());

    Codec<Holder<QuestTypes>> REFERENCE_CODEC = RegistryFileCodec.create(QuestRegistries.Keys.QUEST_TYPE, DIRECT_CODEC);

    Codec<HolderSet<QuestTypes>> LIST_CODEC = RegistryCodecs.homogeneousList(QuestRegistries.Keys.QUEST_TYPE, DIRECT_CODEC);
    Codec<? extends QuestTypes> codec();
}

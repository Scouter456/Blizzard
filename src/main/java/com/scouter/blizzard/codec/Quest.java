package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public interface Quest {
    Codec<Quest> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> QuestRegistries.QUEST_TYPE_SERIALIZER.get().getCodec()).dispatch(Quest::codec, Function.identity());

    Codec<Holder<Quest>> REFERENCE_CODEC = RegistryFileCodec.create(QuestRegistries.Keys.QUEST_TYPE, DIRECT_CODEC);

    Codec<HolderSet<Quest>> LIST_CODEC = RegistryCodecs.homogeneousList(QuestRegistries.Keys.QUEST_TYPE, DIRECT_CODEC);

    boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel);

    CompoundTag getSerializer();

    void setData(CompoundTag tag);


    Codec<? extends Quest> codec();
}

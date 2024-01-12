package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.UUID;
import java.util.function.Function;

public interface Task {
    Codec<Task> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> TaskRegistries.TASK_TYPE_SERIALIZER.get().getCodec()).dispatch(Task::codec, Function.identity());
    Codec<Task> DIRECT_SERIALIZER_CODEC = ExtraCodecs.lazyInitializedCodec(() -> TaskRegistries.TASK_TYPE_SERIALIZER.get().getCodec()).dispatch(Task::serializerCodec, Function.identity());


    Codec<Holder<Task>> REFERENCE_CODEC = RegistryFileCodec.create(TaskRegistries.Keys.TASK_TYPE, DIRECT_CODEC);

    Codec<HolderSet<Task>> LIST_CODEC = RegistryCodecs.homogeneousList(TaskRegistries.Keys.TASK_TYPE, DIRECT_CODEC);

    default boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        return false;
    }

    default boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        return false;
    }

    default boolean playerObtainItem(ItemStack stack, Player brewer, ServerLevel serverLevel) {
        return false;
    }

    default boolean playerFindStructure(Player player, ServerLevel serverLevel) {
        return false;
    }

    ResourceLocation identifier();
    Codec<? extends Task> codec();

    Codec<? extends Task> serializerCodec();
}

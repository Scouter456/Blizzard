package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BreakTask implements Task {
    public int toBreak;
    public Block block;
    public int broken;
    public ResourceLocation id;
    public BreakTask(Block state, int amount, ResourceLocation id) {
        this.block = state;
        this.toBreak = amount;
        this.id = id;
    }

    public BreakTask(Block state, int amount, ResourceLocation id, int mined) {
        this.block = state;
        this.toBreak = amount;
        this.id = id;
        this.broken = mined;
    }

    @Override
    public boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        if(state.is(block)) {
            broken++;
        }
        return false;
    }

    @Override
    public boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerObtainItem(ItemStack stack, Player brewer, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.BREAK_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.BREAK_TASK_SERIALIZER.get();
    }
}

package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MineTask implements Task {
    public int toMine;
    public Block block;
    public int mined;
    public ResourceLocation id;
    public MineTask(Block state, int amount, ResourceLocation id) {
        this.block = state;
        this.toMine = amount;
        this.id = id;
    }

    public MineTask(Block state, int amount, ResourceLocation id, int mined) {
        this.block = state;
        this.toMine = amount;
        this.id = id;
        this.mined = mined;
    }

    @Override
    public boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        if(state.is(block)) {
            mined++;
        }
        return false;
    }

    @Override
    public boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.MINE_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.MINE_TASK_SERIALIZER.get();
    }
}

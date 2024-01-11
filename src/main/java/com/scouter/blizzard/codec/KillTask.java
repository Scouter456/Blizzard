package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class KillTask implements Task {
    public int killedEnties;
    public EntityType<?> entity;
    public EntityType<?> player;
    public int toKill;
    public ResourceLocation id;
    public KillTask(EntityType<?> entity, EntityType<?> player, int toKill, ResourceLocation id) {
        this.entity = entity;
        this.player = player;
        this.toKill = toKill;
        this.id = id;
    }

    public KillTask(EntityType<?> entity, EntityType<?> player, int toKill, ResourceLocation id, int killedEnties) {
        this.entity = entity;
        this.player = player;
        this.toKill = toKill;
        this.id = id;
        this.killedEnties = killedEnties;
    }

    @Override
    public boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        LivingEntity entityT = (LivingEntity) entity.create(serverLevel);
        if (killed.getType() == entityT.getType()) {
            killedEnties++;
        }

        return false;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    public int getKilledEnties() {
        return killedEnties;
    }

    public void setKilledEnties(int killedEnties) {
        this.killedEnties = killedEnties;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.KILL_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.KILL_TASK_SERIALIZER.get();
    }
}


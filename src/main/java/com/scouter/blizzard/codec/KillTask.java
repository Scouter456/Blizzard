package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

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
    public boolean test(TaskData data) {
        ServerLevel serverLevel = data.getServerLevel();
        LivingEntity killed = data.getKilledEntity();
        LivingEntity entityT = (LivingEntity) entity.create(serverLevel);
        if (killed.getType() == entityT.getType()) {
            killedEnties++;
        }
        return false;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.KILL;
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


package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class KillQuest implements Quest {
    private int killedEnties;
    public EntityType<?> entity;
    public EntityType<?> player;
    public int toKill;
    public KillQuest(EntityType<?> entity, EntityType<?> player, int toKill) {
        this.entity = entity;
        this.player = player;
        this.toKill = toKill;
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
    public CompoundTag getSerializer() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("killedEntities", killedEnties);
        return compoundTag;
    }

    @Override
    public void setData(CompoundTag tag) {
        this.killedEnties = tag.getInt("killedEntities");
    }


    public int getKilledEnties() {
        return killedEnties;
    }

    public void setKilledEnties(int killedEnties) {
        this.killedEnties = killedEnties;
    }

    @Override
    public Codec<? extends Quest> codec() {
        return QuestRegistry.KILL_QUEST.get();
    }
}


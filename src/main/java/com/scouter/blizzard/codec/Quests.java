package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class Quests {
    public record KillQuest(EntityType<?> entity,  EntityType<?> player) implements QuestTypes{
        @Override
        public Codec<? extends QuestTypes> codec() {
            return QuestRegistry.KILL_QUEST.get();
        }
    }
}

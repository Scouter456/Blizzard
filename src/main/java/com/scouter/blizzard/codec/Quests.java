package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class Quests {
    public record KillQuest(EntityType<?> entity,  EntityType<?> player, ResourceLocation advancementQuest) implements Quest {

        @Override
        public boolean playerKillEntity(Entity killed, Player attacker, ServerLevel serverLevel) {
            if(entity.create(serverLevel).is(killed)) {
                advanceQuest(attacker);
            }

            return false;
        }

        @Override
        public ResourceLocation getAdvancementQuest() {
            return advancementQuest;
        }

        @Override
        public Codec<? extends Quest> codec() {
            return QuestRegistry.KILL_QUEST.get();
        }
    }
}

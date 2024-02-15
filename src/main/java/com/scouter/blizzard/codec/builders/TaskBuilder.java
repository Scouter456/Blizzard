package com.scouter.blizzard.codec.builders;

import com.scouter.blizzard.codec.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class TaskBuilder {

    public static class KillTaskBuilder {
        private EntityType<?> entityToKill;
        private EntityType<?> killer;
        private int amount = 1;
        private ResourceLocation identifier;
        public static KillTaskBuilder Builder(ResourceLocation id){
            KillTaskBuilder killTaskBuilder = new KillTaskBuilder();
            killTaskBuilder.withIdentifier(id);
            return killTaskBuilder;
        }

        public KillTaskBuilder withEntityToKill(EntityType<?> entityToKill) {
            this.entityToKill = entityToKill;
            return this;
        }

        public KillTaskBuilder withKiller(EntityType<?> killer) {
            this.killer = killer;
            return this;
        }

        public KillTaskBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public KillTaskBuilder withIdentifier(ResourceLocation identifier) {
            this.identifier = identifier;
            return this;
        }

        public KillTask build() {
            return new KillTask(entityToKill, killer, amount, identifier);
        }
    }

    public static class BreakTaskBuilder {
        private Block toBreak;
        private int amount = 1;
        private ResourceLocation identifier;
        public static BreakTaskBuilder Builder(ResourceLocation id){
            BreakTaskBuilder breakTaskBuilder = new BreakTaskBuilder();
            breakTaskBuilder.withIdentifier(id);
            return breakTaskBuilder;
        }

        public BreakTaskBuilder withToBreak(Block toBreak) {
            this.toBreak = toBreak;
            return this;
        }

        public BreakTaskBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public BreakTaskBuilder withIdentifier(ResourceLocation identifier) {
            this.identifier = identifier;
            return this;
        }

        public BreakTask build() {
            return new BreakTask(toBreak, amount, identifier);
        }
    }

    public static class BrewPotionTaskBuilder {
        private Item toBrew;
        private int amount = 1;
        private ResourceLocation identifier;

        public BrewPotionTaskBuilder withToBrew(Item toBrew) {
            this.toBrew = toBrew;
            return this;
        }

        public BrewPotionTaskBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public BrewPotionTaskBuilder withIdentifier(ResourceLocation identifier) {
            this.identifier = identifier;
            return this;
        }

        public BrewPotionTask build() {
            return new BrewPotionTask(toBrew, amount, identifier);
        }
    }

    public static class CollectItemTaskBuilder {
        private Item toCollect;
        private int amount = 1;
        private ResourceLocation identifier;

        public static CollectItemTaskBuilder Builder(ResourceLocation id){
            CollectItemTaskBuilder collectItemTaskBuilder = new CollectItemTaskBuilder();
            collectItemTaskBuilder.withIdentifier(id);
            return collectItemTaskBuilder;
        }

        public CollectItemTaskBuilder withToCollect(Item toCollect) {
            this.toCollect = toCollect;
            return this;
        }

        public CollectItemTaskBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public CollectItemTaskBuilder withIdentifier(ResourceLocation identifier) {
            this.identifier = identifier;
            return this;
        }

        public CollectItemTask build() {
            return new CollectItemTask(toCollect, amount, identifier);
        }
    }

    public static class FindStructureTaskBuilder {
        private ResourceLocation toFind;
        private int amount = 1;
        private ResourceLocation identifier;

        public static FindStructureTaskBuilder Builder(ResourceLocation id){
            FindStructureTaskBuilder findStructureTaskBuilder = new FindStructureTaskBuilder();
            findStructureTaskBuilder.withIdentifier(id);
            return findStructureTaskBuilder;
        }

        public FindStructureTaskBuilder withToFind(ResourceLocation toFind) {
            this.toFind = toFind;
            return this;
        }

        public FindStructureTaskBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public FindStructureTaskBuilder withIdentifier(ResourceLocation identifier) {
            this.identifier = identifier;
            return this;
        }

        public FindStructureTask build() {
            return new FindStructureTask(toFind, amount, identifier);
        }
    }
}

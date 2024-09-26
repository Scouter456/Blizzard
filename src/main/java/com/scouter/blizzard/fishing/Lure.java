package com.scouter.blizzard.fishing;

import com.scouter.blizzard.Blizzard;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Lure {
    private final String name;
    private final String modId;
    private final int catchRate;
    private final ResourceLocation texture;
    private final ResourceLocation lootTableFish;
    private final ResourceLocation lootTableNether;
    private final ResourceLocation lootTableLava;
    private final ResourceLocation lootTableTreasure;
    private final ResourceLocation lootTableTrash;
    private final ResourceLocation lootTableBoss;

    private final TagKey<Block> fishingBlocks;
    private final List<TagKey<Fluid>> fluids;
    private final Supplier<SoundEvent> bobberSplash;
    private final Supplier<SoundEvent> lureBreak;
    private final Supplier<SoundEvent> catchItem;
    private final int fishingRange;
    private final Supplier<ParticleOptions> splashParticle;
    private final Supplier<ParticleOptions> fishingParticle;

    private final Consumer<ServerLevel> playEffect;
    private Lure(String name, String modid, int catchRate, ResourceLocation lootTableNether, int fishingRange, ResourceLocation lootTable, ResourceLocation lootTableLava, ResourceLocation lootTableTreasure, ResourceLocation lootTableTrash, ResourceLocation lootTableBoss, TagKey<Block> fishingBlocks, List<TagKey<Fluid>> fluids, Supplier<SoundEvent> bobberSplash, Supplier<SoundEvent> lureBreak, Supplier<SoundEvent> catchItem, Supplier<ParticleOptions> splashParticle, Supplier<ParticleOptions> fishingParticle, Consumer<ServerLevel> playEffect) {
        this.name = name;
        this.modId = modid;
        this.catchRate = catchRate;
        //todo remove trash, treasure, boss, just lava, nether and normal, the rest can be defined in json!
        this.lootTableNether = lootTableNether;
        this.lootTableFish = lootTable;
        this.lootTableLava = lootTableLava;
        this.lootTableTreasure = lootTableTreasure;
        this.lootTableTrash = lootTableTrash;
        this.lootTableBoss = lootTableBoss;
        this.fishingBlocks = fishingBlocks;
        this.fluids = fluids;
        this.bobberSplash = bobberSplash;
        this.lureBreak = lureBreak;
        this.catchItem = catchItem;
        this.fishingRange = fishingRange;
        this.splashParticle = splashParticle;
        this.fishingParticle = fishingParticle;
        this.playEffect = playEffect;
        this.texture = new ResourceLocation(modid, "textures/entity/lure" + name + "_lure" + ".png");
    }


    public ResourceLocation getLootTableFish() {
        return lootTableFish;
    }

    public ResourceLocation getLootTableTreasure() {
        return lootTableTreasure;
    }

    public ResourceLocation getLootTableLava() {
        return lootTableLava;
    }

    public ResourceLocation getLootTableNether() {
        return lootTableNether;
    }

    public ResourceLocation getLootTableTrash() {
        return lootTableTrash;
    }

    public ResourceLocation getLootTableBoss() {
        return lootTableBoss;
    }

    public Supplier<ParticleOptions> getFishingParticle() {
        return fishingParticle;
    }

    public TagKey<Block> getFishingBlocks() {
        return fishingBlocks;
    }

    public List<TagKey<Fluid>> getFluids() {
        return fluids;
    }

    public void playEffect(ServerLevel level) {
        playEffect.accept(level);
    }

    public static class LureBuilder {

        private String name;
        private String modId = Blizzard.MODID;
        private int catchRate;
        private ResourceLocation lootTableFish = BuiltInLootTables.FISHING;
        private ResourceLocation lootTableNether;
        private ResourceLocation lootTableLava;
        private ResourceLocation lootTableTreasure;
        private ResourceLocation lootTableTrash;
        private ResourceLocation lootTableBoss;
        private TagKey<Block> fishingBlocks;
        private List<TagKey<Fluid>> fluids = new ArrayList<>();
        private Supplier<SoundEvent> bobberSplash = () -> SoundEvents.FISHING_BOBBER_SPLASH;
        private Supplier<SoundEvent> lureBreak = () -> SoundEvents.ITEM_BREAK;
        private Supplier<SoundEvent> catchItem = () -> SoundEvents.AMETHYST_BLOCK_CHIME;
        private int fishingRange = 1024;
        private Supplier<ParticleOptions> splashParticle = () -> ParticleTypes.SPLASH;
        private Supplier<ParticleOptions> fishingParticle = () -> ParticleTypes.FISHING;
        private Consumer<ServerLevel> playEffect = (level) -> {};
        LureBuilder() {
        }

        public LureBuilder(String name) {
            this.name = name;
        }


        public LureBuilder setModid(String modid) {
            this.modId = modid;
            return this;
        }

        public LureBuilder setCatchRate(int catchRate) {
            this.catchRate = catchRate;
            return this;
        }

        public LureBuilder setLootTableFish(ResourceLocation lootTable) {
            this.lootTableFish = lootTable;
            return this;
        }

        public LureBuilder setLootTableNether(ResourceLocation lootTable) {
            this.lootTableNether = lootTable;
            return this;
        }

        public LureBuilder setLootTableLava(ResourceLocation lootTable) {
            this.lootTableLava = lootTable;
            return this;
        }

        public LureBuilder setLootTableTrash(ResourceLocation lootTable) {
            this.lootTableFish = lootTable;
            return this;
        }

        public LureBuilder setLootTableTreasure(ResourceLocation lootTable) {
            this.lootTableTreasure = lootTable;
            return this;
        }

        public LureBuilder setLootTableBoss(ResourceLocation lootTable) {
            this.lootTableBoss = lootTable;
            return this;
        }

        public LureBuilder setFishingBlocks(TagKey<Block> fishingBlocks) {
            this.fishingBlocks = fishingBlocks;
            return this;
        }

        public LureBuilder setFluids(List<TagKey<Fluid>> fluids) {
            this.fluids = fluids;
            return this;
        }

        public LureBuilder setBobberSplash(Supplier<SoundEvent> bobberSplash) {
            this.bobberSplash = bobberSplash;
            return this;
        }

        public LureBuilder setLureBreak(Supplier<SoundEvent> lureBreak) {
            this.lureBreak = lureBreak;
            return this;
        }

        public LureBuilder setCatchItem(Supplier<SoundEvent> catchItem) {
            this.catchItem = catchItem;
            return this;
        }

        public LureBuilder setFishingRange(int fishingRange) {
            this.fishingRange = fishingRange;
            return this;
        }

        public LureBuilder setSplashParticle(Supplier<ParticleOptions> splashParticle) {
            this.splashParticle = splashParticle;
            return this;
        }

        public LureBuilder setFishingParticle(Supplier<ParticleOptions> fishingParticle) {
            this.fishingParticle = fishingParticle;
            return this;
        }

        public LureBuilder setEffect(Consumer<ServerLevel> level) {
            this.playEffect = level;
            return  this;
        }

        public Lure build() {
            return new Lure(
                    this.name,
                    this.modId,
                    this.catchRate,
                    lootTableNether, this.fishingRange,
                    this.lootTableFish, lootTableLava, lootTableTreasure, lootTableTrash, lootTableBoss, this.fishingBlocks,
                    this.fluids,
                    this.bobberSplash,
                    this.lureBreak,
                    this.catchItem,
                    this.splashParticle,
                    this.fishingParticle,
                    playEffect);
        }

    }
    public static Lure SandLure = new LureBuilder("sand").setFishingBlocks(BlockTags.SAND).setCatchRate(1).build();
}

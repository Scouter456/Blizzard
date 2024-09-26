package com.scouter.blizzard.fishing;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class FishingMethods {




    private List<ItemStack> getLoot(Lure lure, LootParams lootParams, ServerLevel serverLevel) {
        ResourceLocation lootTableLocation;
        if(serverLevel.getLevel().dimension() == Level.NETHER) {
            lootTableLocation= lure.getLootTableNether();
        } else {
            lootTableLocation = lure.getLootTableLava();
        }
        //todo add thing for boss loot, treasure and other ones, has to be done in json!!!
        //todo add to json check for weather and place/biome and then add boss loot table and better fishing
        lootTableLocation = lure.getLootTableFish();

        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableLocation);
        return lootTable.getRandomItems(lootParams);
    }

    private ParticleOptions getParticle(Lure lure, BlockState blockState) {
        FluidState fluidState = blockState.getFluidState();
        if(fluidState != null && !fluidState.isEmpty()) {

        }

        return lure.getFishingParticle().get();
    }

    private boolean checkBlock(Lure lure, BlockState blockState) {
        FluidState fluidState = blockState.getFluidState();
        if(fluidState != null && !fluidState.isEmpty() && lure.getFluids().contains(getFluidTagForFluid(fluidState))) {
            return true;
        }
        if(blockState != null && blockState.is(lure.getFishingBlocks())) {
            return true;
        }

        return false;
    }

    private TagKey<Fluid> getFluidTagForFluid(FluidState fluidState) {
        if(fluidState.is(FluidTags.LAVA)) return FluidTags.LAVA;
        return FluidTags.WATER;
    }
}

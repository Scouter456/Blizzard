package com.scouter.blizzard.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {

    @Inject(method = "setDirtAt", at = @At(value = "TAIL"))
    private static void Blizzard$spawnSnowAndIce(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, CallbackInfo ci) {
        if (!(((net.minecraft.world.level.LevelReader) pLevel).getBlockState(pPos).onTreeGrow((net.minecraft.world.level.LevelReader) pLevel, pBlockSetter, pRandom, pPos, pConfig)) && isDirt(pLevel, pPos)) {
            pBlockSetter.accept(pPos, Blocks.DIAMOND_BLOCK.defaultBlockState());
        }
    }

    private static boolean isDirt(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (p_70304_) -> {
            return Feature.isDirt(p_70304_) && !p_70304_.is(Blocks.GRASS_BLOCK) && !p_70304_.is(Blocks.MYCELIUM);
        });
    }
}

package com.scouter.blizzard.mixins;

import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Level.class)
public class LevelMixin {


    @Inject(method = "destroyBlock", at = @At(value = "HEAD"))
    public void blizzard$removeBlock(BlockPos pPos, boolean pDropBlock, @Nullable Entity pEntity, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if(EnchantedBlockData.containsBlock(pPos)) {
            EnchantedBlockData.safeRemove(pPos);
        }
    }

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At(value = "HEAD"))
    public void blizzard$setBlock(BlockPos pPos, BlockState pState, int pFlags, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if((pState.is(Blocks.AIR) || pState.is(Blocks.CAVE_AIR)) && EnchantedBlockData.containsBlock(pPos)) {
            EnchantedBlockData.safeRemove(pPos);
        }
    }
}

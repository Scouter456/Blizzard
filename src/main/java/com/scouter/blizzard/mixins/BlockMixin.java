package com.scouter.blizzard.mixins;

import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {


    @Inject(method = "playerWillDestroy", at = @At(value = "HEAD"))
    public void blizzard$playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, CallbackInfo ci) {
        if(EnchantedBlockData.containsBlock(pPos)) {
            EnchantedBlockData.safeRemove(pPos);
        }
    }
    @Inject(method = "destroy", at = @At(value = "HEAD"))
    public void blizzard$destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState, CallbackInfo ci) {
        if(EnchantedBlockData.containsBlock(pPos)) {
            EnchantedBlockData.safeRemove(pPos);
        }
    }
}

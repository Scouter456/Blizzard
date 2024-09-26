package com.scouter.blizzard.mixins;

import com.scouter.blizzard.events.BlizzardWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {
    @Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
    public void Blizzard$preventMelt(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        BlizzardWorldData data = BlizzardWorldData.get(pLevel);
        ChunkPos pos = new ChunkPos(pPos);
        if(data.hasBlizzard(pos)) {
            ci.cancel();
        }
    }
}

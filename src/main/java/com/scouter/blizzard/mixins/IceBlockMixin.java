package com.scouter.blizzard.mixins;

import com.scouter.blizzard.events.BlizzardServerLevel;
import com.scouter.blizzard.events.BlizzardWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IceBlock.class)
public class IceBlockMixin {

    @Inject(method = "melt", at = @At(value = "HEAD"), cancellable = true)
    public void Blizzard$preventMelt(BlockState pState, Level pLevel, BlockPos pPos, CallbackInfo ci) {
        BlizzardWorldData data = BlizzardWorldData.get(pLevel);
        ChunkPos pos = new ChunkPos(pPos);
        if(data.hasBlizzard(pos)) {
            ci.cancel();
        }
    }

}

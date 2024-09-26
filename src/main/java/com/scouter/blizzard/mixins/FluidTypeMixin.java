package com.scouter.blizzard.mixins;

import com.scouter.blizzard.events.BlizzardWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidType.class)
public class FluidTypeMixin {

    //@Inject(method = "isVaporizedOnPlacement", at = @At(value = "RETURN"), cancellable = true, remap = false)
    //public void blizzard$changeVaporize(Level level, BlockPos pos, FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
    //    if(!level.isClientSide) {
    //        BlizzardWorldData blizzardWorldData = BlizzardWorldData.get(level);
    //        ChunkPos chunkPos = new ChunkPos(pos);
    //        if (blizzardWorldData.hasBlizzard(chunkPos)) {
    //            cir.setReturnValue(false);
    //        }
    //    }
    //}
}

package com.scouter.blizzard.mixins;

import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBEMixin {

    @Inject(method = "getTotalCookTime", at = @At(value = "HEAD"), cancellable = true)
    private static void blizzard$changeCookTime(Level pLevel, AbstractFurnaceBlockEntity pBlockEntity, CallbackInfoReturnable<Integer> cir) {
        BlockPos pos = pBlockEntity.getBlockPos();
        if(EnchantedBlockData.containsBlock(pos)) {
            int time = pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
            int modifiedTime = time / 6;
            cir.setReturnValue(modifiedTime);
        }
    }
}

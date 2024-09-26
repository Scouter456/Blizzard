package com.scouter.blizzard.block;

import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ExtendedHalfTransparentBlock extends Block {
    public ExtendedHalfTransparentBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) ? true : super.skipRendering(pState, pAdjacentBlockState, pSide);
    }
}

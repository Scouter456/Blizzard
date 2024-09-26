package com.scouter.blizzard.enchantedblock;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.HashMap;

public class EnchantedBlockData {
    public static final HashMap<BlockPos, BlockState> ENCHANTED_BLOCKS = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void putBlock(BlockPos pos, BlockState block) {
        ENCHANTED_BLOCKS.put(pos, block);
    }

    public static BlockState getBlock(BlockPos pos) {
        return ENCHANTED_BLOCKS.getOrDefault(pos, Blocks.AIR.defaultBlockState());
    }

    public static void safeRemove(BlockPos pos) {
        if(ENCHANTED_BLOCKS.containsKey(pos)) {
            ENCHANTED_BLOCKS.remove(pos);
            return;
        }
        LOGGER.error("There was a block without an assigned blockpos");
    }

    public static boolean containsBlock(BlockPos pos) {
        return ENCHANTED_BLOCKS.containsKey(pos);
    }
}

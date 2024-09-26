package com.scouter.blizzard.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

public class BlizzardServerLevel {
    public static void spawnSnowAndIce(ServerLevel serverLevel, LevelChunk pChunk, int pRandomTickSpeed) {
        ChunkPos chunkpos = pChunk.getPos();
        int i = chunkpos.getMinBlockX();
        int j = chunkpos.getMinBlockZ();
        ProfilerFiller profilerfiller = serverLevel.getProfiler();
        BlizzardWorldData data = BlizzardWorldData.get(serverLevel);
        boolean hasBlizzard = data.hasBlizzard(chunkpos);
        profilerfiller.popPush("snowAndIce");
        if(serverLevel.getRandom().nextInt(8) == 0) {
            BlockPos blockpos1 = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, serverLevel.getBlockRandomPos(i, 0, j, 15));
            BlockPos blockpos2 = blockpos1.below();
            Biome biome = serverLevel.getBiome(blockpos1).value();
            if (serverLevel.isAreaLoaded(blockpos2, 1)) // Forge: check area to avoid loading neighbors in unloaded chunks
                if (hasBlizzard && shouldFreeze(serverLevel, blockpos2)) {
                    serverLevel.setBlockAndUpdate(blockpos2, Blocks.ICE.defaultBlockState());
                }

            if (hasBlizzard) {
                int i1 = serverLevel.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);
                boolean shouldFreeze = shouldFreeze(serverLevel, blockpos2);
                if (i1 > 0 && serverLevel.getBlockState(blockpos2).getFluidState().isEmpty() && canSurvive(serverLevel, blockpos1)) {
                    BlockState blockstate = serverLevel.getBlockState(blockpos1);
                    if (blockstate.is(Blocks.SNOW)) {
                        int k = blockstate.getValue(LAYERS);
                        if (k < Math.min(i1, 8)) {
                            BlockState blockstate1 = blockstate.setValue(LAYERS, Integer.valueOf(k + 1));
                            Block.pushEntitiesUp(blockstate, blockstate1, serverLevel, blockpos1);
                            serverLevel.setBlockAndUpdate(blockpos1, blockstate1);
                        }
                    } else {
                        serverLevel.setBlockAndUpdate(blockpos1, Blocks.SNOW.defaultBlockState());
                    }
                }

                if (hasBlizzard) {
                    BlockState blockstate3 = serverLevel.getBlockState(blockpos2);
                    blockstate3.getBlock().handlePrecipitation(blockstate3, serverLevel, blockpos2, Biome.Precipitation.SNOW);
                }
            }
        }
    }

    public static boolean shouldFreeze(LevelReader pLevel, BlockPos pPos) {
        return shouldFreeze(pLevel, pPos, true);
    }

    public static boolean shouldFreeze(LevelReader pLevel, BlockPos pWater, boolean pMustBeAtEdge) {
        if (pWater.getY() >= pLevel.getMinBuildHeight() && pWater.getY() < pLevel.getMaxBuildHeight() && pLevel.getBrightness(LightLayer.BLOCK, pWater) < 10) {
            BlockState blockstate = pLevel.getBlockState(pWater);
            FluidState fluidstate = pLevel.getFluidState(pWater);
            if (fluidstate.getType() == Fluids.WATER && blockstate.getBlock() instanceof LiquidBlock) {
                if (!pMustBeAtEdge) {
                    return true;
                }

                boolean flag = pLevel.isWaterAt(pWater.west()) && pLevel.isWaterAt(pWater.east()) && pLevel.isWaterAt(pWater.north()) && pLevel.isWaterAt(pWater.south());
                if (!flag) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }


    public static boolean canSurvive(LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        if (blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else if (blockstate.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)) {
            return true;
        } else {
            return Block.isFaceFull(blockstate.getCollisionShape(pLevel, pPos.below()), Direction.UP) || blockstate.is(Blocks.SNOW) && blockstate.getValue(LAYERS) == 8;
        }
    }
}

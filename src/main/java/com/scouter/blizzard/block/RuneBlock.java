package com.scouter.blizzard.block;

import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RuneBlock extends Block {
    //public static final IntegerProperty X1 = IntegerProperty.create("x_one", 0, 16);
    //public static final IntegerProperty X2 = IntegerProperty.create("x_two", 0, 16);
    //public static final IntegerProperty Y1 = IntegerProperty.create("y_one", 0, 16);
    //public static final IntegerProperty Y2 = IntegerProperty.create("y_twp", 0, 16);
    //public static final IntegerProperty Z1 = IntegerProperty.create("z_one", 0, 16);
    //public static final IntegerProperty Z2 = IntegerProperty.create("z_two", 0, 16);

    public RuneBlock(Properties pProperties) {
        super(pProperties);
        //this.registerDefaultState(this.defaultBlockState().setValue(X1, 0).setValue(Y1, 0).setValue(Z1, 0).setValue(X2, 16).setValue(Y2, 16).setValue(Z2, 16));
    }

    //protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    //    pBuilder.add(X1, X2, Y1, Y2, Z1, Z2);
    //}
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockState state1 = EnchantedBlockData.getBlock(pos);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        dropResources(state1, level,  pos,null,player,stack);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        //BlockState state = EnchantedBlockData.getBlock(pPos);
        //EnchantedBlockData.safeRemove(pPos);
        //pLevel.setBlock(pPos, state, 3);
    }


    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide()) return;
        //BlockState state = EnchantedBlockData.getBlock(pPos);
        EnchantedBlockData.safeRemove(pPos);
        //dropResources(state, (Level) pLevel,  pPos);
        //pLevel.setBlock(pPos, state, 3);
    }
    /*
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(pState.getValue(X1), pState.getValue(Y1), pState.getValue(Z1), pState.getValue(X2), pState.getValue(Y2), pState.getValue(Z2));
    }
*/

    //public static void setShape(VoxelShape shape, BlockState state) {
    //    AABB aabb = shape.bounds();
    //    int x1 = (int) aabb.minX;
    //    int y1 = (int) aabb.minY;
    //    int z1 = (int) aabb.minZ;
    //    int x2 = (int) aabb.maxX;
    //    int y2 = (int) aabb.maxY;
    //    int z2 = (int) aabb.maxZ;
//
    //    state.setValue(X1, x1);
    //    state.setValue(Y1, y1);
    //    state.setValue(Z1, z1);
    //    state.setValue(X2, x2);
    //    state.setValue(Y2, y2);
    //    state.setValue(Z2, z2);
    //}

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
}

package com.scouter.blizzard.block;

import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.util.BTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClearGlassBlock extends AbstractGlassBlock {

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty SHAPE_PROP_UP = BooleanProperty.create("shape_up");
    public static final BooleanProperty SHAPE_PROP_DOWN = BooleanProperty.create("shape_down");
    public static final BooleanProperty SHAPE_PROP_EAST = BooleanProperty.create("shape_east");
    public static final BooleanProperty SHAPE_PROP_WEST = BooleanProperty.create("shape_west");
    public static final BooleanProperty SHAPE_PROP_NORTH = BooleanProperty.create("shape_north");
    public static final BooleanProperty SHAPE_PROP_SOUTH = BooleanProperty.create("shape_south");

    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 15.9D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.1D);
    protected static final VoxelShape SHAPE_EAST = Block.box(15.9D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 15.9D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 0.0D, 0.1D, 16.0D, 16.0D);

    private TagKey<Fluid> fluidTagKey;
    private static final Map<Direction, VoxelShape> occlusionShapes = new HashMap<Direction, VoxelShape>() {{
        put(Direction.DOWN, SHAPE_DOWN);
        put(Direction.UP, SHAPE_UP);
        put(Direction.NORTH, SHAPE_NORTH);
        put(Direction.EAST, SHAPE_EAST);
        put(Direction.SOUTH, SHAPE_SOUTH);
        put(Direction.WEST, SHAPE_WEST);
    }};

    public static VoxelShape DEFAULT_SHAPE = Shapes.empty();

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static Map<BlockState, VoxelShape> SHAPES_CACHE = new ConcurrentHashMap<>();


    protected ClearGlassBlock(TagKey<Fluid> tagKey) {
        super(Properties.of().mapColor(MapColor.METAL).strength(0.2F).sound(SoundType.GLASS));
        this.fluidTagKey = tagKey;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, Boolean.valueOf(false))
                .setValue(DOWN, Boolean.valueOf(false))
                .setValue(EAST, Boolean.valueOf(false))
                .setValue(WEST, Boolean.valueOf(false))
                .setValue(NORTH, Boolean.valueOf(false))
                .setValue(SOUTH, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_UP, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_DOWN, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_EAST, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_WEST, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_NORTH, Boolean.valueOf(false))
                .setValue(SHAPE_PROP_SOUTH, Boolean.valueOf(false))
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_58032_) {
        p_58032_.add(UP, DOWN, NORTH, SOUTH, EAST, WEST, SHAPE_PROP_UP, SHAPE_PROP_DOWN, SHAPE_PROP_NORTH, SHAPE_PROP_SOUTH, SHAPE_PROP_EAST, SHAPE_PROP_WEST);
    }


    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader levelreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        BlockState state = defaultBlockState();
        BlockState state1 = setState(state, blockpos, levelreader);
        setShape(state1, blockpos);
        return state1;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelreader, BlockPos blockpos, BlockPos pos2) {
        BlockState state1 = setState(state, blockpos, levelreader);
        setShape(state1, blockpos);
        return state1;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        VoxelShape shape = getShape(pPos, pState);
        return shape;
    }

    public VoxelShape getShape(BlockPos pos,BlockState state) {
        VoxelShape shape =  SHAPES_CACHE.computeIfAbsent(state, e -> DEFAULT_SHAPE);
        return shape;
    }

    public void setShape(BlockState state, BlockPos blockpos) {
        VoxelShape shape = DEFAULT_SHAPE;

        boolean hasNorth = state.getValue(SHAPE_PROP_NORTH);
        boolean hasEast = state.getValue(SHAPE_PROP_EAST);
        boolean hasSouth = state.getValue(SHAPE_PROP_SOUTH);
        boolean hasWest = state.getValue(SHAPE_PROP_WEST);
        boolean hasUp = state.getValue(SHAPE_PROP_UP);
        boolean hasDown = state.getValue(SHAPE_PROP_DOWN);

        if(hasNorth) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.NORTH));
        }

        if(hasEast) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.EAST));
        }

        if(hasSouth) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.SOUTH));
        }

        if(hasWest) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.WEST));
        }

        if(hasUp) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.UP));
        }

        if(hasDown) {
            shape = Shapes.or(shape, occlusionShapes.get(Direction.DOWN));
        }

        SHAPES_CACHE.put(state, shape);
    }
    public BlockState setState(BlockState state, BlockPos blockpos, LevelReader levelreader){
        BlockPos n = blockpos.north();
        BlockPos e = blockpos.east();
        BlockPos s = blockpos.south();
        BlockPos w = blockpos.west();
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();

        BlockState northState = levelreader.getBlockState(n);
        BlockState eastState = levelreader.getBlockState(e);
        BlockState southState = levelreader.getBlockState(s);
        BlockState westState = levelreader.getBlockState(w);
        BlockState upState = levelreader.getBlockState(u);
        BlockState downState = levelreader.getBlockState(d);

        FluidState northFluidState = levelreader.getFluidState(n);
        FluidState eastFluidState = levelreader.getFluidState(e);
        FluidState southFluidState = levelreader.getFluidState(s);
        FluidState westFluidState = levelreader.getFluidState(w);
        FluidState upFluidState = levelreader.getFluidState(u);
        FluidState downFluidState = levelreader.getFluidState(d);

        BlockState state1 = state
                .setValue(NORTH, northState.is(this))
                .setValue(EAST, eastState.is(this))
                .setValue(SOUTH, southState.is(this))
                .setValue(WEST, westState.is(this))
                .setValue(UP, upState.is(this))
                .setValue(DOWN, downState.is(this));

        TagKey<Fluid> tagKey = fluidTagKey;

        boolean northValue = northFluidState.is(tagKey);
        boolean eastValue = eastFluidState.is(tagKey);
        boolean southValue = southFluidState.is(tagKey);
        boolean westValue = westFluidState.is(tagKey);
        boolean upValue = upFluidState.is(tagKey);
        boolean downValue = downFluidState.is(tagKey);


        BlockState state2 = state1
                .setValue(SHAPE_PROP_NORTH, northValue)
                .setValue(SHAPE_PROP_EAST, eastValue)
                .setValue(SHAPE_PROP_SOUTH, southValue)
                .setValue(SHAPE_PROP_WEST,westValue)
                .setValue(SHAPE_PROP_UP, upValue)
                .setValue(SHAPE_PROP_DOWN, downValue);

        //setShape(state, blockpos);
        return state2;
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }
}

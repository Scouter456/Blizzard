package com.scouter.blizzard.block;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.items.BItems;
import com.scouter.blizzard.util.BTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBlocks {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Blizzard.MODID);
    public static final RegistryObject<Block> CLEAR_VIEW_BLOCK = registerBlock("clear_view_block", () -> new ClearGlassBlock(FluidTags.WATER));
    public static final RegistryObject<Block> RUNE_BLOCK = registerBlock("rune_block", () -> new RuneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.2F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> EMPTY = registerBlock("empty", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.2F).sound(SoundType.GLASS).noOcclusion()));
    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        BItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }


    public static <B extends Block> RegistryObject<B> registerBlockNoItem(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        return block;
    }

}

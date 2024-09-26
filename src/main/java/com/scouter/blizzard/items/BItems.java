package com.scouter.blizzard.items;


import com.scouter.blizzard.Blizzard;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Blizzard.MODID);


    //public static final RegistryObject<Item> CHELLERB_POLLEN = registerItem("chellerb_pollen", new Item.Properties());
    public static RegistryObject<Item> registerItem(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new Item(properties));
    }

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(),new Item.Properties()));
    }

    public static RegistryObject<Item> fromBlock(Block block) {
        return ITEMS.register(ForgeRegistries.BLOCKS.getKey(block).getPath(), () -> new BlockItem(block,new Item.Properties()));
    }
}

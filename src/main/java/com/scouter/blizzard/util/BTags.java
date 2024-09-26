package com.scouter.blizzard.util;

import com.scouter.blizzard.Blizzard;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;

public class BTags {
    public static final TagKey<Fluid> CLEAR_VIEW_GLASS_WATER = registerFluidTag("clear_view_glass_water");

    private static TagKey<Fluid> registerFluidTag(String name){
        return TagKey.create(Registries.FLUID, new ResourceLocation(Blizzard.MODID, name));
    }
}

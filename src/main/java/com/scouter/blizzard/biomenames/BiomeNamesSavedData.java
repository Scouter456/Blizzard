package com.scouter.blizzard.biomenames;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.PalettedContainer;

import java.util.*;

public class BiomeNamesSavedData {
    public static final Multimap<ResourceKey<Biome>, BlockPos> blockPosBiomeMap = HashMultimap.create();
    public static final Map<BlockPos, String> blockPosStringMap = new HashMap();
    public static final List<String> biomeNames = Arrays.asList(
            "Celestial Canopy",
            "Whispering Meadows",
            "Mystic Tundra",
            "Luminous Reef",
            "Enchanted Savannah",
            "Ember Grove",
            "Radiant Plateau",
            "Quicksilver Oasis",
            "Ephemeral Rainforest",
            "Astral Abyss",
            "Crimson Desolation",
            "Nebula Shores",
            "Serene Alpine",
            "Azure Highlands"
    );

    public static String getName(ResourceKey<Biome> biomeResourceKey, BlockPos pos) {
        Collection<BlockPos> posCollection = blockPosBiomeMap.get(biomeResourceKey);
        BlockPos closestPos = posCollection.stream()
                .min(Comparator.comparingDouble(pos1 -> pos1.distSqr(pos)))
                .orElse(null);
        if(closestPos != null && closestPos.distSqr(pos) >= 400) {
            closestPos = null;
        }

        if(closestPos != null) {
            String name = blockPosStringMap.get(closestPos);
            blockPosBiomeMap.put(biomeResourceKey, pos);
            blockPosStringMap.put(pos, name);
            return name;
        } else {
            String name = getRandomBiomeName();
            blockPosBiomeMap.put(biomeResourceKey, pos);
            blockPosStringMap.put(pos, name);
            return name;
        }
    }

    private static String getRandomBiomeName() {
        Random random = new Random();
        int index = random.nextInt(biomeNames.size());
        return biomeNames.get(index);
    }
}

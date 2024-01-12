package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;

public class FindStructureTask implements Task{

    public ResourceLocation structure;
    private ResourceKey<Structure> resourceKey;
    public int toFind;
    public ResourceLocation id;
    public int found;

    public FindStructureTask(ResourceLocation structure, int toFind, ResourceLocation id) {
        ResourceKey<Structure> structureResourceKey = ResourceKey.create(Registries.STRUCTURE, structure);
        this.structure = structure;
        this.resourceKey = structureResourceKey;
        this.toFind = toFind;
        this.id = id;
    }

    public FindStructureTask(ResourceLocation structure, int toFind, ResourceLocation id, int found) {
        ResourceKey<Structure> structureResourceKey = ResourceKey.create(Registries.STRUCTURE, structure);
        this.structure = structure;
        this.toFind = toFind;
        this.resourceKey = structureResourceKey;
        this.id = id;
        this.found = found;

    }

    @Override
    public boolean playerFindStructure(Player player, ServerLevel serverLevel) {
        boolean inStructure = StructureGatherer.isInStructure(serverLevel, player.blockPosition(), resourceKey);
        if(inStructure) {
            found++;
        }

        return false;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.FIND_STRUCTURE_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.FIND_STRUCTURE_TASK_SERIALIZER.get();
    }
}

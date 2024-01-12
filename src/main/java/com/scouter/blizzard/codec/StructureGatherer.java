package com.scouter.blizzard.codec;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class StructureGatherer {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isInStructure(ServerLevel serverLevel, BlockPos pos, ResourceKey<Structure> structure) {
        return LocationPredicate.inStructure(structure).matches(serverLevel, pos.getX(), pos.getY(), pos.getZ());
    }

    public static AABB getAABBFromBoundingBox(BoundingBox boundingBox) {
        return new AABB(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
    }

    public static BlockPos getMinBlockPosFromAABB(AABB aabb) {
        return BlockPos.containing(aabb.minX, aabb.minY, aabb.minZ);
    }

    public static BlockPos getMaxBlockPosFromAABB(AABB aabb) {
        return BlockPos.containing(aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public static boolean checkIfSafeForCheck(StructureStart structure) {
        return structure.getPieces().size() > 0;
    }

    public static boolean checkForEntityInsideStructure(ServerLevel serverLevel, AABB aabb, EntityType entity) {
        List<Entity> entityList = serverLevel.getEntitiesOfClass(Entity.class, aabb);
        for (var e : entityList) {
            if (e.getType() == entity) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkForEntityInsideStructure(ServerLevel serverLevel, AABB aabb, TagKey<EntityType<?>> entity) {
        List<Entity> entityList = serverLevel.getEntitiesOfClass(Entity.class, aabb);
        for (var e : entityList) {
            if (e.getType().is(entity)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkForLivingEntityInsideStructure(ServerLevel serverLevel, AABB aabb, EntityType entity) {
        List<LivingEntity> livingEntityList = serverLevel.getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity e : livingEntityList) {
            if (e.getType() == entity) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkIfLivingEntityFromTagIsInStructure(ServerLevel serverLevel, AABB aabb, TagKey<EntityType<?>> entity) {
        List<LivingEntity> livingEntityList = serverLevel.getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity e : livingEntityList) {
            if (e.getType().is(entity)) {
                return true;
            }
        }
        return false;
    }

    public static boolean findBoundingBoxForAllStructuresAndCheckForBoss(ServerLevel serverLevel, BlockPos blockPos) {
        StructureManager structureManager = serverLevel.structureManager();

        Map<Structure, LongSet> structuresAtPos = structureManager.getAllStructuresAt(blockPos);


        BoundingBox boundingBox = new BoundingBox(0, 0, 0, 0, 0, 0);
        if (structuresAtPos.isEmpty()) {
            return false;
        }

       //for (Structure structureL : structuresAtPos.keySet()) {
       //    //ResourceKey<Structure> key = structureManager.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).getResourceKey(structureL).get();
       //    //boolean inStructure = StructureGatherer.isInStructure(serverLevel, pos, key);
       //    StructureStart structure = structureManager.getStructureAt(blockPos, structureL);
       //    if (!structure.isValid() || structure.getPieces().contains(DRTags.StructureTag.DUNGEON_STRUCTURES_LIST)) {
       //        return false;
       //    }

       //    if (StructureGatherer.checkIfSafeForCheck(structure)) {
       //        boundingBox = structure.getBoundingBox();
       //    }

       //    AABB aabb = StructureGatherer.getAABBFromBoundingBox(boundingBox);
       //    return StructureGatherer.checkIfLivingEntityFromTagIsInStructure(serverLevel, aabb, DRTags.EntityTypes.DUNGEON_BOSSES);
       //}

        return false;
    }
}

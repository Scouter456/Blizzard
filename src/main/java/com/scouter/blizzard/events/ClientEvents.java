package com.scouter.blizzard.events;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.block.BBlocks;
import com.scouter.blizzard.enchantedblock.BRenderType;
import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.*;

import static com.scouter.blizzard.Blizzard.prefix;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ClientEvents {

    private static final Logger LOGGER = LogUtils.getLogger();

    final static Minecraft minecraft = Minecraft.getInstance();

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event)
    {
        if(minecraft == null) return;
        Player player = minecraft.player;
        Level level = player.level();
        Vec3 vec3 = player.getEyePosition();
        //BlockState blockState = level.getBlockState(BlockPos.containing(vec3.x, vec3.y, vec3.z));
        boolean isCreative = player.isCreative();
        boolean isSpectator = player.isSpectator();
        ChunkPos chunkPos = new ChunkPos(player.getBlockX() >>4, player.getBlockZ()>> 4);
        BlizzardData data = BlizzardClientData.getBlizzardClientData(level.dimension(), chunkPos);
        if(data.isHasBlizzard() && (event.getMode().equals(FogRenderer.FogMode.FOG_TERRAIN)) && isCreative && !isSpectator)
        {
            float maxFloatValue = 50;
            float maxFarPlaneDistance = 40F; // Set your maximum far plane distance
            float minNearPlaneDistance = 20F; // Set your minimum near plane distance
            float maxNearPlaneDistance = 40F; // Set your maximum near plane distance

            // Normalize blizzard strength between 0 and 1
            float normalizedStrength = Math.max(0.0F, Math.min(1.0F, data.getBlizzardStrength() / maxFloatValue));

            // Calculate dynamic fog distances
            float farPlaneDistance = maxFarPlaneDistance - normalizedStrength * (maxFarPlaneDistance - minNearPlaneDistance);
            float nearPlaneDistance = maxNearPlaneDistance - normalizedStrength * (maxNearPlaneDistance - minNearPlaneDistance);

            event.setFarPlaneDistance(farPlaneDistance);
            event.setNearPlaneDistance(nearPlaneDistance);
            event.setFogShape(FogShape.CYLINDER);
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void fogColors(ViewportEvent.ComputeFogColor event) {
        if(minecraft == null) return;
        Player player = minecraft.player;
        Level level = player.level();
        Vec3 vec3 = player.getEyePosition();
        //BlockState blockState = level.getBlockState(BlockPos.containing(vec3.x, vec3.y, vec3.z));
        boolean isCreative = player.isCreative();
        boolean isSpectator = player.isSpectator();
        ChunkPos chunkPos = new ChunkPos(player.getBlockX() >>4, player.getBlockZ()>> 4);
        BlizzardData data = BlizzardClientData.getBlizzardClientData(level.dimension(), chunkPos);
        boolean inBlizzard = data.isHasBlizzard();
        if(inBlizzard && isCreative && !isSpectator)
        {
            float[] colors = BlizzardWeatherRenderer.getRGBFromFloat(data.getBlizzardStrength());
            event.setRed(colors[0]);
            event.setGreen(colors[1]);
            event.setBlue(colors[2]);
        }
    }


    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {

            Minecraft minecraft = Minecraft.getInstance();
            ClientLevel level = minecraft.level;
            if (level == null)
                return;

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource bufferSource = minecraft.renderBuffers().bufferSource();

            Player player = minecraft.player;
            if (player == null || player.isSpectator())
                return;
            int renderDistance = minecraft.options.renderDistance().get() * 32;
            List<BlockPos> blocksInRenderDistance = EnchantedBlockData.ENCHANTED_BLOCKS.keySet()
                    .stream()
                    .filter(pos -> player.blockPosition().distSqr(pos) <= renderDistance)
                    .toList();

            for (BlockPos pos : blocksInRenderDistance) {
                BlockState state = EnchantedBlockData.getBlock(pos);
                //renderBlockOverlay(pos, state, RenderType.cutout(), poseStack, bufferSource);
                renderBlockOverlay(pos, BBlocks.EMPTY.get().defaultBlockState(), BRenderType.getGlintCutout(), poseStack, bufferSource);


            }
        }
    }

    public static void renderBlockOverlay(BlockPos blockPos, BlockState state, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource) {
        Minecraft minecraft = Minecraft.getInstance();

        ClientLevel level = minecraft.level;

        BlockRenderDispatcher blockRenderDispatcher = minecraft.getBlockRenderer();
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.pushPose();

        poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
        poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        poseStack.scale(1.002F, 1.002F,1.002F);
        poseStack.translate(-0.0016F, -0.0016F, -0.0016F);

        BakedModel blockModel = blockRenderDispatcher.getBlockModel(state);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(
                level,
                blockModel,
                state,
                blockPos,
                poseStack,
                bufferSource.getBuffer(type),
                true,
                RandomSource.create(),
                state.getSeed(blockPos),
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                type);

        poseStack.popPose();
    }



    public static void renderBlock(Level world, BlockState state, BlockPos pos, float alpha, PoseStack ms, MultiBufferSource buffers) {
        if (pos != null) {
            ms.pushPose();
            ms.translate(pos.getX(), pos.getY(), pos.getZ());

            if (state.getBlock() == Blocks.AIR) {
                float scale = 0.3F;
                float off = (1F - scale) / 2;
                ms.translate(off, off, -off);
                ms.scale(scale, scale, scale);

                state = Blocks.RED_CONCRETE.defaultBlockState();
            }

            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY);

            ms.popPose();
        }
    }

}
//for (var renderType : renderTypes)
//{
//    blockRenderDispatcher.getModelRenderer().tesselateBlock(
//            level,
//            blockModel,
//            state,
//            blockPos,
//            poseStack,
//            bufferSource.getBuffer(renderType),
//            true,
//            RandomSource.create(),
//            state.getSeed(blockPos),
//            OverlayTexture.NO_OVERLAY,
//            ModelData.EMPTY,
//            renderType);
//}
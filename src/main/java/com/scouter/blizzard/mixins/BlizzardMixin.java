package com.scouter.blizzard.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.scouter.blizzard.events.BlizzardClientData;
import com.scouter.blizzard.events.BlizzardData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

@Mixin(LevelRenderer.class)
public class BlizzardMixin {

    private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
    private static final Logger LOGGER = LogUtils.getLogger();
    @Shadow
    @Nullable
    private ClientLevel level;
    //TODO for loop on player render distance and render chunks with blizzards
    //TODO also fix the blizzard showing one way
    @Inject(method = "renderSnowAndRain", at = @At(value = "TAIL"))
    public void renderSnowAndRain(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo ci) {
        int x = (int) pCamX >> 4;
        int z = (int) pCamZ >> 4;
        ChunkPos chunkPos = new ChunkPos(x, z);
        BlizzardData data = BlizzardClientData.getBlizzardClientData(chunkPos);
        int i1 = -1;
        if (data.isHasBlizzard()) {
            int l = 5;
            if (Minecraft.useFancyGraphics()) {
                l = 10;
            }
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            double d0 = (double) data.getBlizzardStrength() * 0.5D;
            double d1 = (double) data.getBlizzardStrength() * 0.5D;
            LevelRenderer levelRenderer = (LevelRenderer) (Object) this;
            Level level = Minecraft.getInstance().level;
            float f1 = (float) levelRenderer.getTicks() + pPartialTick;
            int i = Mth.floor(pCamX);
            int j = Mth.floor(pCamY);
            int k = Mth.floor(pCamZ);
            int f = data.getBlizzardStrength();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int j1 = k - l; j1 <= k + l; ++j1) {
                for (int k1 = i - l; k1 <= i + l; ++k1) {
                    int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
                    int j2 = j - l;
                    int k2 = j + l;
                    if (j2 < i2) {
                        j2 = i2;
                    }

                    if (k2 < i2) {
                        k2 = i2;
                    }

                    int l2 = i2;
                    if (i2 < j) {
                        l2 = j;
                    }


                    if (i1 != 1) {
                        if (i1 >= 0) {
                            tesselator.end();
                        }

                        i1 = 1;
                        RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }
                    RandomSource randomsource = RandomSource.create((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));

                    float f5 = -((float) (levelRenderer.getTicks() & 511) + pPartialTick) / 512.0F;
                    float f6 = (float) (randomsource.nextDouble() + (double) f1 * 0.01D * (double) ((float) randomsource.nextGaussian()));
                    float f7 = (float) (randomsource.nextDouble() + (double) (f1 * (float) randomsource.nextGaussian()) * 0.001D);
                    double d3 = (double) k1 + 0.5D - pCamX;
                    double d5 = (double) j1 + 0.5D - pCamZ;
                    float f8 = (float) Math.sqrt(d3 * d3 + d5 * d5) / (float) l;
                    float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * f;
                    blockpos$mutableblockpos.set(k1, l2, j1);
                    int k3 = getLightColor(level, blockpos$mutableblockpos);
                    int l3 = k3 >> 16 & '\uffff';
                    int i4 = k3 & '\uffff';
                    int j4 = (l3 * 3 + 240) / 4;
                    int k4 = (i4 * 3 + 240) / 4;
                    bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                    bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                }
            }

            if (i1 >= 0) {
                tesselator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            pLightTexture.turnOffLightLayer();
        }

    }
}

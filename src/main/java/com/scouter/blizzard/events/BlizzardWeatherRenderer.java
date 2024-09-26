package com.scouter.blizzard.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.events.particles.BParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import static com.scouter.blizzard.Blizzard.prefix;
import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

public class BlizzardWeatherRenderer {

    private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation DENSE_SNOW_LOCATION = prefix("textures/environment/dense_snow.png");

    private static int blizzardSoundTime;

    public static boolean renderSnowParticleAndPlaySound(LevelRenderer levelRenderer, Camera camera) {
        renderParticleAndSound(levelRenderer, camera);
        return true;
    }

    public static boolean renderSnowAndRain(LevelRenderer levelRenderer, LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ) {
        //Minecraft mc = Minecraft.getInstance();
        renderBlizzard(levelRenderer, pLightTexture, pPartialTick, pCamX, pCamY, pCamZ);
        return true;
    }

    private static void renderBlizzard(LevelRenderer levelRenderer, LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ) {
        if (isNearBlizzard(pCamX, pCamZ)) {
            pLightTexture.turnOnLightLayer();
            int l = 10;
            if (Minecraft.useFancyGraphics()) {
                l = 20;
            }
            Level level = Minecraft.getInstance().level;
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int i1 = -1;
            double d0 = (double) 1 * 0.5D;
            double d1 = (double) 1 * 0.5D;

            float f1 = (float) levelRenderer.getTicks() + pPartialTick;
            int i = Mth.floor(pCamX);
            int j = Mth.floor(pCamY);
            int k = Mth.floor(pCamZ);
            int f = 1;
            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            RenderSystem.setShader(GameRenderer::getParticleShader);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int j1 = k - l; j1 <= k + l; ++j1) {
                for (int k1 = i - l; k1 <= i + l; ++k1) {

                    //Replace biome check with chunk check
                    ChunkPos chunkPos = new ChunkPos(k1 >> 4, j1 >> 4);
                    BlizzardData data = BlizzardClientData.getBlizzardClientData(level.dimension(), chunkPos);
                    if (data.isHasBlizzard()) {
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

                        //Only use single texture
                        if (i1 != 0) {
                            if (i1 >= 0) {
                                tesselator.end();
                            }

                            i1 = 0;

                            RenderSystem.setShaderTexture(0, DENSE_SNOW_LOCATION);
                            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                        }
                        RandomSource randomsource = RandomSource.create((long) (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                        float f5 = -((float) (levelRenderer.getTicks() & 511) + pPartialTick) / 512.0F;

                        float blizzardStrength = data.getBlizzardStrength();
                        float[] colors = getRGBFromFloat(blizzardStrength);
                        float red = colors[0];
                        float green = colors[1];
                        float blue = colors[2];

                        float f6 = (float) (randomsource.nextDouble() + (double) f1 * 0.01D * blizzardStrength * (double) ((float) randomsource.nextGaussian()));
                        float f7 = (float) (randomsource.nextDouble() + (double) (f1 * (float) randomsource.nextGaussian()) * 0.001D * blizzardStrength);
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
                        bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float) j2 * 0.25F + f5 + f7).color(red, green, blue, f9).uv2(k4, j4).endVertex();
                        bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) k2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float) j2 * 0.25F + f5 + f7).color(red, green, blue, f9).uv2(k4, j4).endVertex();
                        bufferbuilder.vertex((double) k1 - pCamX + d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float) k2 * 0.25F + f5 + f7).color(red, green, blue, f9).uv2(k4, j4).endVertex();
                        bufferbuilder.vertex((double) k1 - pCamX - d0 + 0.5D, (double) j2 - pCamY, (double) j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float) k2 * 0.25F + f5 + f7).color(red, green, blue, f9).uv2(k4, j4).endVertex();
                    }
                }
            }

            if (i1 == 0) {
                tesselator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            pLightTexture.turnOffLightLayer();
        }
    }

    private static void renderParticleAndSound(LevelRenderer levelRenderer, Camera camera) {
        BlockPos blockpos = BlockPos.containing(camera.getPosition());
        if (isNearBlizzard(blockpos.getX(), blockpos.getZ())) {
            Minecraft minecraft = Minecraft.getInstance();
            Level levelreader = minecraft.level;
            RandomSource randomsource = RandomSource.create((long) levelRenderer.getTicks() * 312987231L);
            BlockPos blockpos1 = null;
            int i = (int) (1000.0F) / (minecraft.options.particles().get() == ParticleStatus.DECREASED ? 8 : 1);
            for (int j = 0; j < i; ++j) {
                int k = randomsource.nextInt(21) - 10;
                int l = randomsource.nextInt(21) - 10;
                BlockPos blockpos2 = levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
                ChunkPos chunkPos = new ChunkPos(blockpos2);
                BlizzardData data = BlizzardClientData.getBlizzardClientData(levelreader.dimension(), chunkPos);
                if (blockpos2.getY() > levelreader.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && data.isHasBlizzard()) {
                    Biome biome = levelreader.getBiome(blockpos2).value();
                    //if (biome.getPrecipitationAt(blockpos2) == Biome.Precipitation.RAIN) {
                    blockpos1 = blockpos2.below();
                    if (minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
                        break;
                    }

                    double d0 = randomsource.nextDouble();
                    double d1 = randomsource.nextDouble();
                    BlockState blockstate = levelreader.getBlockState(blockpos1);
                    FluidState fluidstate = levelreader.getFluidState(blockpos1);
                    VoxelShape voxelshape = blockstate.getCollisionShape(levelreader, blockpos1);
                    double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
                    double d3 = (double) fluidstate.getHeight(levelreader, blockpos1);
                    double d4 = Math.max(d2, d3);
                    ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? BParticles.SNOW_SPLASH.get() : ParticleTypes.SMOKE;
                    levelreader.addParticle(particleoptions, (double) blockpos1.getX() + d0, (double) blockpos1.getY() + d4, (double) blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);
                    //}
                }
            }
            //todo maybe put soundTime in BlizzardData instead, seems appropriate
            if (blockpos1 != null && randomsource.nextInt(3) < BlizzardWeatherRenderer.blizzardSoundTime++) {
                BlizzardWeatherRenderer.blizzardSoundTime = 0;
                if (blockpos1.getY() > blockpos.getY() + 1 && levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float) blockpos.getY())) {
                    levelreader.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
                } else {
                    levelreader.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }


    private static boolean isNearBlizzard(double xIn, double zIn) {
        final int range = 5;
        int x = (int) xIn >> 4;
        int z = (int) zIn >> 4;
        Level level = Minecraft.getInstance().level;
        boolean nearBlizzard = false;
        for (int chunkX = -range; (chunkX < range) && !nearBlizzard; chunkX++) {
            for (int chunkZ = -range; (chunkZ < range) && !nearBlizzard; chunkZ++) {
                ChunkPos chunkPos = new ChunkPos(x + chunkX, z + chunkZ);
                BlizzardData data = BlizzardClientData.getBlizzardClientData(level.dimension(), chunkPos);
                nearBlizzard = data.isHasBlizzard();
            }
        }
        return nearBlizzard;
    }

    private final static float MAX_VALUE = 50F;

    public static float[] getRGBFromFloat(float blizzardStrength) {
        // Ensure the input value is within the valid range [0, 1]


        float normalizedStrength = Math.max(0.0F, Math.min(1.0F, blizzardStrength / MAX_VALUE));

        // Start color: #FFFFFF (white)
        float startRed = 1.0F;
        float startGreen = 1.0F;
        float startBlue = 1.0F;

        // End color: #A5FBFF (light blue)

        float endRed = 0.6471F;
        float endGreen = 0.9843F;
        float endBlue = 1.0F;

        // Interpolate between start and end colors based on normalized strength
        float red = startRed + (endRed - startRed) * normalizedStrength;
        float green = startGreen + (endGreen - startGreen) * normalizedStrength;
        float blue = startBlue + (endBlue - startBlue) * normalizedStrength;

        float[] colors = new float[3];
        colors[0] = red;
        colors[1] = green;
        colors[2] = blue;
        return colors;
    }
}

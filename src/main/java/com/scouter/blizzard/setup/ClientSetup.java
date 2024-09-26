package com.scouter.blizzard.setup;


import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.enchantedblock.BInternalShaders;
import com.scouter.blizzard.entities.BEntities;
import com.scouter.blizzard.entities.ModelLocations;
import com.scouter.blizzard.entities.model.FishModel;
import com.scouter.blizzard.entities.renderer.FishRenderer;
import com.scouter.blizzard.events.particles.BParticles;
import com.scouter.blizzard.events.particles.SnowDropParticle;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import java.io.IOException;

import static com.scouter.blizzard.Blizzard.prefix;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(FMLClientSetupEvent event) {
        EntityRenderers.register(BEntities.MOTOR_FISH.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.MOTOR_FISH)));
        EntityRenderers.register(BEntities.EMERALDINE_MOLA.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.EMERALDINE_MOLA)));
        EntityRenderers.register(BEntities.SCARLET_MINNOW.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.SCARLET_MINNOW)));
        EntityRenderers.register(BEntities.LAZULI_SCALE.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.LAZULI_SCALE)));
        EntityRenderers.register(BEntities.AUREOFIN.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.AUREOFIN)));
        EntityRenderers.register(BEntities.GLITTERING_FISH.get(), e -> new FishRenderer<>(e, new FishModel<>(ModelLocations.GLITTERING_FISH)));

    }


    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {

    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(),prefix("rendertype_cutout_glint"), DefaultVertexFormat.BLOCK), BInternalShaders::setEnchantedCutoutGlint);

            LOGGER.info("registered internal shaders");
        } catch (IOException exception) {
            LOGGER.error("could not register internal shaders");
            exception.printStackTrace();
        }
    }


    @SubscribeEvent
    public static void registerParticleTypes(RegisterParticleProvidersEvent event){
        // Minecraft.getInstance().particleEngine.register(NDUParticle.GLOWDINE_PARTICLE.get(), GlowdineParticle.GlowdineProvider::new);
        event.registerSpriteSet(BParticles.SNOW_SPLASH.get(), SnowDropParticle.Provider::new);

    }
}


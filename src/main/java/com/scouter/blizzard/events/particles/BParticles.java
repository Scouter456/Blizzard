package com.scouter.blizzard.events.particles;

import com.scouter.blizzard.Blizzard;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Blizzard.MODID);

    public static final RegistryObject<SimpleParticleType> SNOW_SPLASH = PARTICLE_TYPES.register("snow_splash", () -> new SimpleParticleType(false));

}

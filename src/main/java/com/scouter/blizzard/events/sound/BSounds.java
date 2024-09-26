package com.scouter.blizzard.events.sound;

import com.scouter.blizzard.Blizzard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Blizzard.MODID);
    public static final RegistryObject<SoundEvent> WEATHER_BLIZZARD = createSoundEvent("weather.blizzard");
    public static final RegistryObject<SoundEvent> WEATHER_BLIZZARD_ABOVE = createSoundEvent("weather.blizzard.above");
    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Blizzard.MODID, soundName)));
    }
}

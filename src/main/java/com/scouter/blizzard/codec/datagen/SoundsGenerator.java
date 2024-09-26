package com.scouter.blizzard.codec.datagen;

import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.events.sound.BSounds;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

import static com.scouter.blizzard.Blizzard.prefix;

public class SoundsGenerator extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    protected SoundsGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, Blizzard.MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(BSounds.WEATHER_BLIZZARD.get(), SoundDefinition.definition().with(SoundDefinition.Sound.sound(prefix("blizzard_1"), SoundDefinition.SoundType.SOUND)).subtitle("weather.blizzard"));
        add(BSounds.WEATHER_BLIZZARD_ABOVE.get(), SoundDefinition.definition().with(SoundDefinition.Sound.sound(prefix("blizzard_1"), SoundDefinition.SoundType.SOUND)).subtitle("weather.blizzard.above"));
    }
}

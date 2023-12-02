package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ClientEvents {

    private static final Logger LOGGER = LogUtils.getLogger();

}

package com.scouter.blizzard;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.codec.QuestCommand;
import com.scouter.blizzard.codec.Task;
import com.scouter.blizzard.codec.TaskRegistries;
import com.scouter.blizzard.events.BlizzardCommand;
import com.scouter.blizzard.events.ClientEvents;
import com.scouter.blizzard.events.ForgeEvents;
import com.scouter.blizzard.setup.ClientSetup;
import com.scouter.blizzard.setup.ModSetup;
import com.scouter.blizzard.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Blizzard.MODID)
public class Blizzard
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "blizzard";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Blizzard()
    {
        Registration.init();
        ModSetup.setup();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(ClientEvents.class);
            // static method with no client-only classes in method signature
        }
        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        modbus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(TaskRegistries.Keys.TASK_TYPE, Task.DIRECT_CODEC);
        });

        GeckoLib.initialize();
    }

    public void commands(RegisterCommandsEvent e) {
        QuestCommand.register(e.getDispatcher());
        BlizzardCommand.register(e.getDispatcher());
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}

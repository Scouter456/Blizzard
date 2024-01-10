package com.scouter.blizzard.codec;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scouter.blizzard.Blizzard.prefix;

public class QuestManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson STANDARD_GSON = new Gson();
    protected static Map<ResourceLocation, Quest> data = new HashMap<>();
    private final String folderName;
    public QuestManager()
    {
        this(prefix("quests").getPath(), STANDARD_GSON);
    }


    public QuestManager(String folderName, Gson gson)
    {
        super(gson, folderName);
        this.folderName = folderName;
    }



    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        Map<ResourceLocation, Quest> questMap = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();

            // if we fail to parse json, log an error and continue
            Quest.DIRECT_CODEC.decode(JsonOps.INSTANCE, element)
                    .get()
                    .ifLeft(result -> {
                        Quest quest = result.getFirst();
                        questMap.put(key, quest);
                    })
                    .ifRight(partial -> LOGGER.error("Failed to parse data json for {} due to: {}", key, partial.message()));

        }

        this.data = questMap;
        LOGGER.info("Data loader for {} loaded {} jsons", this.folderName, this.data.size());
    }
}
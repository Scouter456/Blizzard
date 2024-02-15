package com.scouter.blizzard.codec.datagen;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.scouter.blizzard.codec.Quests;
import com.scouter.blizzard.codec.Task;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.scouter.blizzard.Blizzard.prefix;

public abstract class QuestProvider implements DataProvider {
    protected final PackOutput.PathProvider questPathProvider;

    public QuestProvider(PackOutput pOutput) {
        this.questPathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, prefix("quests").getPath());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Set<ResourceLocation> taskSet = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildQuests((quests -> {
            if (!set.add(quests.getQuestResourceLocation())) {
                throw new IllegalStateException("Duplicate quest " + quests.getQuestResourceLocation());
            } else {
                for(Task task : quests.getTasks()) {
                    if(!taskSet.add(task.identifier()))
                        throw new IllegalStateException("Duplicate task " +  task.identifier()  + "in quest " + quests.getQuestResourceLocation());
                }
                list.add(DataProvider.saveStable(pOutput, quests.serializeQuest(), this.questPathProvider.json(quests.getQuestResourceLocation())));
            }
        }));
        return CompletableFuture.allOf(list.toArray((p_253414_) -> {
            return new CompletableFuture[p_253414_];
        }));
    }


    protected abstract void buildQuests(Consumer<Quests> pWriter);

    @Override
    public String getName() {
        return "quests";
    }
}

package com.scouter.blizzard.codec.datagen;

import com.scouter.blizzard.codec.KillTask;
import com.scouter.blizzard.codec.Quests;
import com.scouter.blizzard.codec.Task;
import com.scouter.blizzard.codec.builders.QuestsBuilder;
import com.scouter.blizzard.codec.builders.TaskBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static com.scouter.blizzard.Blizzard.prefix;

public class QuestBuilderProvider extends QuestProvider {
    public QuestBuilderProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildQuests(Consumer<Quests> pWriter) {
        Task task = TaskBuilder.BreakTaskBuilder.Builder(prefix("test_task")).withToBreak(Blocks.SAND).withAmount(2).build();
        Task taskgrass = TaskBuilder.BreakTaskBuilder.Builder(prefix("test_task_grass")).withToBreak(Blocks.GRASS).withAmount(2).build();
        Task finditem = TaskBuilder.CollectItemTaskBuilder.Builder(prefix("test_task_item")).withToCollect(Items.ANVIL).withAmount(2).build();

        Quests quests = QuestsBuilder.Builder().withResourceLocation(prefix("test"))
                .withQuestName("test")
                .asRootQuest(true)
                .withAttachEntity(EntityType.ALLAY)
                .withTask(task)
                .withTask(taskgrass)
                .withTask(finditem)
                .build();

        pWriter.accept(quests);
    }
}

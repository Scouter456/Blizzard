package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CollectItemTask implements Task{


    public Item item;
    public int toCollect;
    public ResourceLocation id;
    public int collected;
    public CollectItemTask(Item item, int amount, ResourceLocation id){
        this.item = item;
        this.toCollect = amount;
        this.id = id;
    }

    public CollectItemTask(Item item, int amount, ResourceLocation id, int brewed){
        this.item = item;
        this.toCollect = amount;
        this.id = id;
        this.collected = brewed;
    }

    @Override
    public boolean test(TaskData data) {
        ItemStack stack = data.getObtainedItem();
        if(stack.is(item)) {
            collected++;
        }

        return false;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.COLLECT;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.COLLECT_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.COLLECT_TASK_SERIALIZER.get();
    }
}

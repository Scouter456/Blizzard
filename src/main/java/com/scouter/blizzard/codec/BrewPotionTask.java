package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BrewPotionTask implements Task{


    public Item potion;
    public int toBrew;
    public ResourceLocation id;
    public int brewed;
    public BrewPotionTask(Item potion, int amount, ResourceLocation id){
        this.potion = potion;
        this.toBrew = amount;
        this.id = id;
    }

    public BrewPotionTask(Item potion, int amount, ResourceLocation id, int brewed){
        this.potion = potion;
        this.toBrew = amount;
        this.id = id;
        this.brewed = brewed;
    }

    @Override
    public boolean test(TaskData data) {
        ItemStack stack = data.getObtainedItem();
        if(stack.is(potion)) {
            brewed++;
        }

        return false;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.BREW;
    }

    @Override
    public ResourceLocation identifier() {
        return id;
    }

    @Override
    public Codec<? extends Task> codec() {
        return TaskRegistry.BREW_TASK.get();
    }

    @Override
    public Codec<? extends Task> serializerCodec() {
        return TaskRegistry.BREW_TASK_SERIALIZER.get();
    }
}

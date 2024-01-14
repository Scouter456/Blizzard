package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

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
    public boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerBrewPotion(ItemStack stack, Player brewer, ServerLevel serverLevel) {
        if(stack.is(potion)) {
            brewed++;
        }

        return false;
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

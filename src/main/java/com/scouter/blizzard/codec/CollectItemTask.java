package com.scouter.blizzard.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

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
    public boolean playerMineBlock(BlockState state, Player player, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerKillEntity(LivingEntity killed, Player attacker, ServerLevel serverLevel) {
        return false;
    }

    @Override
    public boolean playerObtainItem(ItemStack stack, Player brewer, ServerLevel serverLevel) {
        if(stack.is(item)) {
            collected++;
        }

        return false;
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

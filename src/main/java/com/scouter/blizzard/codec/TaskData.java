package com.scouter.blizzard.codec;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.Optional;

public class TaskData {
    private static final Logger LOGGER = LogUtils.getLogger();
    private ServerLevel serverLevel;
    private Player player;
    private Optional<BlockState> minedBlock;

    private Optional<LivingEntity> killedEntity;
    private Optional<ItemStack> obtainedItem;


    public TaskData(ServerLevel serverLevel, Player player) {
        this.serverLevel = serverLevel;
        this.player = player;
    }

    public void setMinedBlock(BlockState minedBlock) {
        this.minedBlock =  Optional.of(minedBlock);
    }

    public void setKilledEntity(LivingEntity killedEntity) {
        this.killedEntity = Optional.of(killedEntity);
    }

    public void setObtainedItem(ItemStack obtainedItem) {
        this.obtainedItem = Optional.of(obtainedItem);
    }

    public ServerLevel getServerLevel() {
        return serverLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockState getMinedBlock() {
        return minedBlock.orElseThrow(() -> new IllegalStateException("Tried getting mined block for player with ID " + player.getStringUUID()));
    }

    public LivingEntity getKilledEntity() {
        return killedEntity.orElseThrow(() -> new IllegalStateException("Tried getting killed entity for player with ID " + player.getStringUUID()));
    }

    public ItemStack getObtainedItem() {
        return obtainedItem.orElseThrow(() -> new IllegalStateException("Tried getting obtained item for player with ID " + player.getStringUUID()));
    }
}

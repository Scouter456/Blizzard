package com.scouter.blizzard.events;

import com.mojang.logging.LogUtils;
import com.scouter.blizzard.Blizzard;
import com.scouter.blizzard.biomelevels.BiomeLevelData;
import com.scouter.blizzard.biomenames.BiomeNamesSavedData;
import com.scouter.blizzard.block.BBlocks;
import com.scouter.blizzard.block.RuneBlock;
import com.scouter.blizzard.codec.*;
import com.scouter.blizzard.enchantedblock.EnchantedBlockData;
import com.scouter.blizzard.message.QuestsS2C;
import com.scouter.blizzard.message.RootQuestsS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.*;

@Mod.EventBusSubscriber(modid = Blizzard.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, ResourceKey<Biome>> playerBiomeCache = new HashMap<>();
    private static int counter;

    @SubscribeEvent
    public static void createBlizzard(TickEvent.PlayerTickEvent event) {
        ChunkPos chunkPos = event.player.chunkPosition();
        //BlizzardData clientData = BlizzardClientData.getBlizzardClientData(chunkPos);
        //LOGGER.info("data " + clientData.getBlizzardStrength());
        //LOGGER.info("data2 " + clientData.isHasBlizzard());
        if (!(event.player instanceof ServerPlayer serverPlayer) || event.phase == TickEvent.Phase.END) return;


        if (!serverPlayer.level().isClientSide && counter++ % 40 == 0) {
            chunkPos = serverPlayer.chunkPosition();
            if (serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.DIAMOND_BLOCK)) {
                BlizzardWorldData blizzardWorldData = BlizzardWorldData.get((ServerLevel) serverPlayer.level());
                BlizzardData blizzardData = new BlizzardData(10, true, chunkPos);
                blizzardWorldData.setBlizzardData(chunkPos, blizzardData);
            }
            if (serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.GOLD_BLOCK)) {
                BlizzardWorldData blizzardWorldData = BlizzardWorldData.get((ServerLevel) serverPlayer.level());
                boolean hasBlizzard = blizzardWorldData.hasBlizzard(chunkPos);
                if (hasBlizzard) {
                    BlizzardData blizzardData = blizzardWorldData.getBlizzardData(chunkPos);
                    blizzardData.setBlizzardStrength(blizzardData.getBlizzardStrength() + 10);
                    blizzardWorldData.setBlizzardData(chunkPos, blizzardData);
                }

            }

            if (serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.STICK)) {
                ServerLevel level = (ServerLevel) serverPlayer.level();
                BiomeLevelData data = BiomeLevelData.get(level);
                ChunkPos pos = serverPlayer.chunkPosition();
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getMiddleBlockX(), pos.getMiddleBlockZ());
                Holder<Biome> biomeHolder = level.getBiome(pos.getMiddleBlockPosition(y));
                ResourceKey<Biome> biomeResourceKey = biomeHolder.unwrapKey().get();
                int biomeLevel = data.getBiomeLevel(level, biomeResourceKey, pos);
                LOGGER.info("Biome: {}, Level: {}", biomeResourceKey, biomeLevel);
            }
            if (serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(Items.WOODEN_SHOVEL)) {
                ServerLevel level = (ServerLevel) serverPlayer.level();
                BiomeLevelData data = BiomeLevelData.get(level);
                ChunkPos pos = serverPlayer.chunkPosition();
                data.addBiomeLevel(pos);
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getMiddleBlockX(), pos.getMiddleBlockZ());
                Holder<Biome> biomeHolder = level.getBiome(pos.getMiddleBlockPosition(y));
                ResourceKey<Biome> biomeResourceKey = biomeHolder.unwrapKey().get();
                int biomeLevel = data.getBiomeLevel(level, biomeResourceKey, pos);
                LOGGER.info("Biome: {}, Level: {}", biomeResourceKey, biomeLevel);
            }


            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(serverLevel, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(serverPlayer.getUUID());
            TaskData data = new TaskData(serverLevel, serverPlayer);
            for (Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.FIND_STRUCTURE);
                for (Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();

            UUID playerId = serverPlayer.getUUID();
            ResourceKey<Biome> biomeResourceKey = serverLevel.getBiome(serverPlayer.blockPosition()).unwrapKey().get();
            ResourceKey<Biome> cachedBiome = playerBiomeCache.get(playerId);
            if (cachedBiome != biomeResourceKey) {
                playerBiomeCache.put(playerId, biomeResourceKey);
                sendBiomeMessage(serverPlayer, biomeResourceKey);
            } else {
                BiomeNamesSavedData.getName(biomeResourceKey, serverPlayer.blockPosition());
            }

            Set<BlockPos> posSet = EnchantedBlockData.ENCHANTED_BLOCKS.keySet();
            for (BlockPos pos : posSet) {
                BlockState state = serverLevel.getBlockState(pos);
                if (state.is(Blocks.CAKE)) {
                    int val = state.getValue(CakeBlock.BITES);
                    if (val > 0) {
                        BlockState state1 =  state.setValue(CakeBlock.BITES, val - 1);
                        serverLevel.setBlockAndUpdate(pos, state1);
                    }

                }
            }

        }
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        BlockState state = event.getLevel().getBlockState(event.getPos());
        //todo add custom check with tag
        if (stack.is(Items.STICK) && player.getUsedItemHand() == InteractionHand.MAIN_HAND && !state.is(BBlocks.RUNE_BLOCK.get())) {

            VoxelShape shape = state.getShape(event.getLevel(), event.getPos());

            BlockState state1 = BBlocks.RUNE_BLOCK.get().defaultBlockState();
            ;
            //RuneBlock.setShape(shape, state1);
            EnchantedBlockData.putBlock(event.getPos(), state1);

            //event.getLevel().setBlock(event.getPos(), BBlocks.RUNE_BLOCK.get().defaultBlockState(), 3);
        }
    }
    //@SubscribeEvent
    //public static void removeBlock(BlockEvent.BreakEvent event) {
    //    BlockPos pos = event.getPos();


    //    if(EnchantedBlockData.containsBlock(pos)) {
    //        EnchantedBlockData.safeRemove(pos);
    //    }
    //}

    @SubscribeEvent
    public static void lavaMovementSpeed(TickEvent.PlayerTickEvent event) {
        if (event.player == null || event.player.isSpectator()) {
            return;
        }
        double d0 = 0.08D;
        boolean flag = event.player.getDeltaMovement().y <= 0.0D;
        //if (flag && event.player.hasEffect(MobEffects.SLOW_FALLING)) {
        //    d0 = 0.01;
        //}
        Player player = event.player;
        Vec3 vec36 = player.getDeltaMovement();

        BlockPos pos = player.blockPosition();
        boolean enchanted = EnchantedBlockData.containsBlock(pos);
        if (enchanted && player.horizontalCollision && player.onClimbable()) {

            vec36 = new Vec3(vec36.x, 0.6D, vec36.z);
            float f4 = player.isSprinting() ? 0.9F : 0.8F;
            player.setDeltaMovement(vec36.multiply((double)f4, (double)0.8F, (double)f4));
            Vec3 vec32 = player.getFluidFallingAdjustedMovement(d0, flag, player.getDeltaMovement());
            player.setDeltaMovement(vec32);

        }
    }

    @SubscribeEvent
    public static void preventPistonPush(PistonEvent.Pre event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        BlockPos pos = event.getFaceOffsetPos();
        if (EnchantedBlockData.containsBlock(pos)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void checkForExplostion(ExplosionEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        List<BlockPos> blockPosList = event.getExplosion().getToBlow();
        List<BlockPos> toNotDestroy = new ArrayList<>();

        blockPosList.forEach(blocks -> {
            if (EnchantedBlockData.containsBlock(blocks)) {
                toNotDestroy.add(blocks);
            }
        });

        event.getExplosion().getToBlow().removeAll(toNotDestroy);
    }

    @SubscribeEvent
    public static void preventBreak(PlayerEvent.BreakSpeed event) {
        if (event.getPosition().isPresent()) {
            BlockPos pos = event.getPosition().get();
            if (EnchantedBlockData.containsBlock(pos)) {
                event.setCanceled(true);
            }
        }
    }

    private static void sendBiomeMessage(ServerPlayer player, ResourceKey<Biome> biomeResourceKey) {
        String name = BiomeNamesSavedData.getName(biomeResourceKey, player.blockPosition());
        player.sendSystemMessage(Component.literal(name), true);
    }

    @SubscribeEvent
    public static void synchBlizzard(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() == null || event.getEntity().level().isClientSide) return;
        ServerLevel level = (ServerLevel) event.getEntity().level();
        ServerPlayer player = (ServerPlayer) event.getEntity();
        BlizzardWorldData data = BlizzardWorldData.get(level);
        BMessages.sendToPlayer(new BlizzardMapS2C(data.getBlizzardData(), level.dimension()), player);
    }

    @SubscribeEvent
    public static void synchBlizzard(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() == null || event.getEntity().level().isClientSide) return;
        ServerLevel level = (ServerLevel) event.getEntity().level().getServer().getLevel(event.getTo());
        ServerPlayer player = (ServerPlayer) event.getEntity();
        BlizzardWorldData data = BlizzardWorldData.get(level);
        BMessages.sendToPlayer(new BlizzardMapS2C(data.getBlizzardData(), event.getTo()), player);
    }


    @SubscribeEvent
    public static void killTask(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source != null && !source.level().isClientSide() && source instanceof ServerPlayer player) {
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, player.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, player);
            data.setKilledEntity(event.getEntity());
            for (Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.KILL);
                for (Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void breakTask(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockState state = event.getState();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setMinedBlock(state);
            for (Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.BREAK);
                for (Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void collectItemTask(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getEntity();
        if (player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getStack();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setObtainedItem(stack);
            for (Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.COLLECT);
                for (Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void brewPotionTask(PlayerBrewedPotionEvent event) {
        Player player = event.getEntity();
        if (player != null && !player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getStack();
            ServerLevel level = (ServerLevel) player.level();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, serverPlayer.getUUID());
            Set<Quests> questsSet = playerQuestManager.getQuestsForUUID(player.getUUID());
            TaskData data = new TaskData(level, serverPlayer);
            data.setObtainedItem(stack);
            for (Quests quests : questsSet) {
                List<Task> taskList = quests.getTaskForType(TaskType.BREW);
                for (Task task : taskList) {
                    task.test(data);
                }
            }
            playerQuestManager.setDirty();
        }
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new QuestManager());
    }

    @SubscribeEvent
    public static void openQuests(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        ServerLevel serverLevel = (ServerLevel) event.getLevel();
        String id = entity.getEncodeId();
        ResourceLocation resourceLocation = new ResourceLocation(id);
        //TODO first check if this entity didnt have quests assigned before so we can fetch the old quests only then can we assign new quests to the entity.
        Map<ResourceLocation, Quests> questsMap = QuestManager.getRootQuestsForEntity(resourceLocation);
        //TODO now assign quest or do something ^^

    }


    @SubscribeEvent
    public static void synchDataPack(OnDatapackSyncEvent event) {
        ServerPlayer player = event.getPlayer();
        List<ServerPlayer> playerList = event.getPlayerList().getPlayers();
        Map<ResourceLocation, Map<ResourceLocation, Quests>> rootQuests = QuestManager.getRootQuests();
        Map<ResourceLocation, Quests> quests = QuestManager.getQuests();

        if (player != null) {
            BMessages.sendToPlayer(new QuestsS2C(quests), player);
            BMessages.sendToPlayer(new RootQuestsS2C(rootQuests), player);
        }


        if (playerList != null && !playerList.isEmpty()) {
            for (ServerPlayer player1 : playerList) {
                BMessages.sendToPlayer(new QuestsS2C(quests), player1);
                BMessages.sendToPlayer(new RootQuestsS2C(rootQuests), player1);
            }
        }
    }
}


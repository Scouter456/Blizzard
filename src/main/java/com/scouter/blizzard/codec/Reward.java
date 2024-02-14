package com.scouter.blizzard.codec;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.scouter.blizzard.Blizzard.prefix;

public interface Reward {

    public static final BiMap<ResourceLocation, Codec<? extends Reward>> CODECS = HashBiMap.create();

    default void generateLoot(ServerLevel level, Quests quests, Player player, Consumer<ItemStack> list) {
    }

    default void generateExperience(ServerLevel level, Quests quests, Player player) {
    }

    void appendHoverText(Consumer<Component> list);

    private static void register(String id, Codec<? extends Reward> codec) {
        CODECS.put(prefix(id), codec);
    }

    public static record QuestRewards(ItemRewards itemRewards, ExperienceReward experienceReward) {
        public static Codec<QuestRewards> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemRewards.CODEC.optionalFieldOf("item_reward", ItemRewards.getDefaultReward()).forGetter(QuestRewards::itemRewards),
                ExperienceReward.CODEC.optionalFieldOf("experience_reward", ExperienceReward.getDefaultReward()).forGetter(QuestRewards::experienceReward))
                .apply(inst, QuestRewards::new)
        );
    }


    public static record ItemRewards(Item item, int count) implements Reward  {

        public static Codec<ItemRewards> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemRewards::item),
                Codec.INT.fieldOf("count").forGetter(ItemRewards::count)).apply(inst, ItemRewards::new)
        );

        @Override
        public void generateLoot(ServerLevel level, Quests quests, Player player, Consumer<ItemStack> list) {
            ItemStack stack = item.getDefaultInstance();
            stack.setCount(count);
            list.accept(stack.copy());
        }

        @Override
        public void appendHoverText(Consumer<Component> list) {
            ItemStack stack = item.getDefaultInstance();
            stack.setCount(count);
            list.accept(Component.translatable("quest.reward.item", stack.getCount(), stack.getHoverName()));
        }

        public static ItemRewards getDefaultReward() {
            return new ItemRewards(ItemStack.EMPTY.getItem(), 0);
        }
    }


    public static record ExperienceReward(int count) implements Reward  {

        public static Codec<ExperienceReward> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("count").forGetter(ExperienceReward::count)).apply(inst, ExperienceReward::new)
        );

        @Override
        public void generateExperience(ServerLevel level, Quests quests, Player player) {
            int completionXp = count;
            while (completionXp > 0) {
                int i = 5;
                completionXp -= i;
                level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), i));
            }
        }

        @Override
        public void appendHoverText(Consumer<Component> list) {
            list.accept(Component.translatable("quest.reward.experience", count));
        }


        public static ExperienceReward getDefaultReward() {
            return new ExperienceReward(0);
        }
    }
}

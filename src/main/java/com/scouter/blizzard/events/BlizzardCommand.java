package com.scouter.blizzard.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.scouter.blizzard.codec.PlayerQuestManager;
import com.scouter.blizzard.codec.QuestManager;
import com.scouter.blizzard.codec.Quests;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

public class BlizzardCommand {

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("blizzards").requires(s -> s.hasPermission(2));

        builder.then(Commands.literal("clear_blizzards").executes(c -> {
            return clearBlizzards(c);
        }));

        builder.then(Commands.argument("clear_blizzard_pos", Vec3Argument.vec3()).executes(c -> {
            return clearBlizzardsAtPos(c, Vec3Argument.getVec3(c, "clear_blizzard_pos"));
        }));

        pDispatcher.register(builder);
    }

    public static int clearBlizzards(CommandContext<CommandSourceStack> c) {
        try {
            ServerLevel level = c.getSource().getLevel();
            level.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("blizzards.clearing_blizzards.all").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC), true);
            BlizzardWorldData data = BlizzardWorldData.get(level);
            data.clearBlizzards();
        } catch (Exception ex) {
            c.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            ex.printStackTrace();
        }
        return 0;
    }

    public static int clearBlizzardsAtPos(CommandContext<CommandSourceStack> c, Vec3 pos) {
        try {
            ServerLevel level = c.getSource().getLevel();
            ChunkPos chunkPos = new ChunkPos((int) pos.x, (int) pos.z);
            level.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("blizzards.clearing_blizzards.pos").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC), true);
            BlizzardWorldData data = BlizzardWorldData.get(level);
            data.clearBlizzardAtPos(chunkPos);
        } catch (Exception ex) {
            c.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            ex.printStackTrace();
        }
        return 0;
    }
}

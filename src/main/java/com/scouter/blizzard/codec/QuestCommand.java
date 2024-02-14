package com.scouter.blizzard.codec;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public class QuestCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPE = (ctx, builder) -> {
        return SharedSuggestionProvider.suggest(QuestManager.getQuests().keySet().stream().map(ResourceLocation::toString), builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("quest").requires(s -> s.hasPermission(2));


        builder.then(Commands.literal("give").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("type", ResourceLocationArgument.id()).suggests(SUGGEST_TYPE).executes(c -> {
            return giveQuest(c, EntityArgument.getPlayer(c, "target"), ResourceLocationArgument.getId(c, "type"));
        }))));


        pDispatcher.register(builder);
    }

    public static int giveQuest(CommandContext<CommandSourceStack> c, ServerPlayer player, ResourceLocation type) {
        try {
            ServerLevel level = c.getSource().getLevel();
            PlayerQuestManager playerQuestManager = PlayerQuestManager.get(level, player.getUUID());
            Quests quests = QuestManager.getQuests().get(type);
            if(quests != null) {
                playerQuestManager.addQuestToUUID(player.getUUID(), quests);
            }

        } catch (Exception ex) {
            c.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            ex.printStackTrace();
        }
        return 0;
    }
}

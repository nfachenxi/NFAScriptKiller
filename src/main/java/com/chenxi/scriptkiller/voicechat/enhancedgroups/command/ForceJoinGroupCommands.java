package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsManager;
import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsVoicechatPlugin;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ForceJoinGroupCommands {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> voicechatNode) {
        voicechatNode.then(
                Commands.literal("forcejoingroup")
                        .requires(source -> PermissionHelper.hasPermission(source, EnhancedGroupsManager.CONFIG.FORCE_JOIN_GROUP_COMMAND_PERMISSION.get()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ForceJoinGroupCommands::execute)
                        )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer executor = context.getSource().getPlayerOrException();
            ServerPlayer player = EntityArgument.getPlayer(context, "player");

            if (EnhancedGroupsVoicechatPlugin.SERVER_API == null) {
                context.getSource().sendFailure(Component.literal("Voice chat not connected"));
                return 0;
            }

            VoicechatConnection executorConnection = EnhancedGroupsVoicechatPlugin.SERVER_API.getConnectionOf(executor.getUUID());

            if (executorConnection == null) {
                context.getSource().sendFailure(Component.literal("Voice chat not connected"));
                return 0;
            }

            VoicechatConnection playerConnection = EnhancedGroupsVoicechatPlugin.SERVER_API.getConnectionOf(player.getUUID());

            if (playerConnection == null) {
                context.getSource().sendFailure(Component.literal("Player is not connected to voice chat"));
                return 0;
            }

            if (!executorConnection.isInGroup()) {
                context.getSource().sendFailure(Component.literal("You are not in a group"));
                return 0;
            }
            Group group = executorConnection.getGroup();

            playerConnection.setGroup(group);

            context.getSource().sendSuccess(() -> Component.literal("Player successfully joined your group"), false);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to force join: " + e.getMessage()));
            return 0;
        }
    }
}

package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsManager;
import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsVoicechatPlugin;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class InstantGroupCommands {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> voicechatNode) {
        voicechatNode.then(
                Commands.literal("instantgroup")
                        .requires(source -> PermissionHelper.hasPermission(source, EnhancedGroupsManager.CONFIG.INSTANT_GROUP_COMMAND_PERMISSION.get()))
                        .executes(InstantGroupCommands::executeDefault)
                        .then(Commands.argument("range", DoubleArgumentType.doubleArg(1.0))
                                .executes(InstantGroupCommands::executeWithRange))
        );
    }

    private static int executeDefault(CommandContext<CommandSourceStack> context) {
        return execute(context, EnhancedGroupsManager.CONFIG.DEFAULT_INSTANT_GROUP_RANGE.get());
    }

    private static int executeWithRange(CommandContext<CommandSourceStack> context) {
        double range = DoubleArgumentType.getDouble(context, "range");
        return execute(context, range);
    }

    private static int execute(CommandContext<CommandSourceStack> context, double range) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            if (EnhancedGroupsVoicechatPlugin.SERVER_API == null) {
                context.getSource().sendFailure(Component.literal("Voice chat not connected"));
                return 0;
            }

            VoicechatConnection playerConnection = EnhancedGroupsVoicechatPlugin.SERVER_API.getConnectionOf(player.getUUID());

            if (playerConnection == null) {
                context.getSource().sendFailure(Component.literal("Voice chat not connected"));
                return 0;
            }

            Group group;
            if (playerConnection.isInGroup()) {
                group = playerConnection.getGroup();
            } else {
                group = EnhancedGroupsVoicechatPlugin.SERVER_API.groupBuilder()
                        .setName(EnhancedGroupsManager.CONFIG.INSTANT_GROUP_NAME.get())
                        .setType(Group.Type.OPEN)
                        .build();
            }

            List<ServerPlayer> players = player.level().getEntitiesOfClass(
                    ServerPlayer.class,
                    new AABB(
                            player.position().x - range, player.position().y - range, player.position().z - range,
                            player.position().x + range, player.position().y + range, player.position().z + range
                    )
            );

            int addedCount = 0;
            for (ServerPlayer p : players) {
                VoicechatConnection connection = EnhancedGroupsVoicechatPlugin.SERVER_API.getConnectionOf(p.getUUID());
                if (connection == null || connection.getGroup() != null) {
                    continue;
                }
                connection.setGroup(group);
                addedCount++;
            }

            final int finalAddedCount = addedCount;
            context.getSource().sendSuccess(() -> Component.literal("Added " + finalAddedCount + " player(s) to instant group"), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to create instant group: " + e.getMessage()));
            return 0;
        }
    }
}

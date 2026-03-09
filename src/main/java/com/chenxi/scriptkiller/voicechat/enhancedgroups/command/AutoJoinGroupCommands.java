package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsManager;
import com.chenxi.scriptkiller.voicechat.enhancedgroups.config.PersistentGroup;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class AutoJoinGroupCommands {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> voicechatNode) {
        voicechatNode.then(
                Commands.literal("autojoingroup")
                        .requires(source -> PermissionHelper.hasPermission(source, EnhancedGroupsManager.CONFIG.AUTO_JOIN_GROUP_COMMAND_PERMISSION.get()))
                        .then(Commands.literal("set")
                                .then(Commands.argument("group_name", StringArgumentType.string())
                                        .executes(ctx -> setByName(ctx, StringArgumentType.getString(ctx, "group_name"), null))
                                        .then(Commands.argument("password", StringArgumentType.string())
                                                .executes(ctx -> setByName(ctx, StringArgumentType.getString(ctx, "group_name"), StringArgumentType.getString(ctx, "password")))
                                        )
                                )
                                .then(Commands.argument("id", UuidArgument.uuid())
                                        .executes(ctx -> setById(ctx, UuidArgument.getUuid(ctx, "id"), null))
                                        .then(Commands.argument("password", StringArgumentType.string())
                                                .executes(ctx -> setById(ctx, UuidArgument.getUuid(ctx, "id"), StringArgumentType.getString(ctx, "password")))
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .executes(AutoJoinGroupCommands::remove)
                        )
                        .then(Commands.literal("global")
                                .requires(source -> PermissionHelper.hasPermission(source, EnhancedGroupsManager.CONFIG.AUTO_JOIN_GROUP_GLOBAL_COMMAND_PERMISSION.get()))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("group_name", StringArgumentType.string())
                                                .executes(ctx -> setGlobalByName(ctx, StringArgumentType.getString(ctx, "group_name")))
                                        )
                                        .then(Commands.argument("id", UuidArgument.uuid())
                                                .executes(ctx -> setGlobalById(ctx, UuidArgument.getUuid(ctx, "id")))
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .executes(AutoJoinGroupCommands::removeGlobal)
                                )
                                .then(Commands.literal("force")
                                        .then(Commands.literal("true")
                                                .executes(ctx -> setForce(ctx, true))
                                        )
                                        .then(Commands.literal("false")
                                                .executes(ctx -> setForce(ctx, false))
                                        )
                                )
                        )
        );
    }

    private static int setByName(CommandContext<CommandSourceStack> context, String groupName, @Nullable String password) {
        PersistentGroup persistentGroup = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(groupName);
        if (persistentGroup == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }
        return autoJoin(context, persistentGroup.getId(), password);
    }

    private static int setById(CommandContext<CommandSourceStack> context, UUID groupId, @Nullable String password) {
        return autoJoin(context, groupId, password);
    }

    private static int remove(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.removePlayerGroup(player.getUUID());
            context.getSource().sendSuccess(() -> Component.literal("Auto join successfully removed"), false);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to remove auto join: " + e.getMessage()));
            return 0;
        }
    }

    private static int autoJoin(CommandContext<CommandSourceStack> context, UUID groupId, @Nullable String password) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            PersistentGroup group = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(groupId);
            if (group == null) {
                context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
                return 0;
            }

            if (group.getPassword() != null && (password == null || !password.equals(group.getPassword()))) {
                context.getSource().sendFailure(Component.literal("Wrong password"));
                return 0;
            }

            EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.setPlayerGroup(player.getUUID(), group.getId());
            context.getSource().sendSuccess(() -> Component.literal("You will now automatically connect to group '" + group.getName() + "' when joining"), false);
            if (EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.getGlobalGroupForced()) {
                context.getSource().sendSystemMessage(Component.literal("Note: Global auto join is currently enforced, meaning that your custom auto join won't have any effect"));
            }
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to set auto join: " + e.getMessage()));
            return 0;
        }
    }

    private static int setGlobalByName(CommandContext<CommandSourceStack> context, String groupName) {
        PersistentGroup persistentGroup = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(groupName);
        if (persistentGroup == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }
        return autoJoinGlobal(context, persistentGroup.getId());
    }

    private static int setGlobalById(CommandContext<CommandSourceStack> context, UUID groupId) {
        return autoJoinGlobal(context, groupId);
    }

    private static int removeGlobal(CommandContext<CommandSourceStack> context) {
        EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.removeGlobalGroup();
        context.getSource().sendSuccess(() -> Component.literal("Global auto join successfully removed"), false);
        return 1;
    }

    private static int setForce(CommandContext<CommandSourceStack> context, boolean status) {
        EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.setGlobalGroupForced(status);
        if (status) {
            context.getSource().sendSuccess(() -> Component.literal("Global auto join is enforced from now on"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Global auto join is not enforced anymore"), false);
        }
        return 1;
    }

    private static int autoJoinGlobal(CommandContext<CommandSourceStack> context, UUID groupId) {
        PersistentGroup group = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(groupId);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }

        if (group.getPassword() != null) {
            context.getSource().sendFailure(Component.literal("Global auto join groups can't be password-protected"));
            return 0;
        }

        EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.setGlobalGroup(group.getId());
        context.getSource().sendSuccess(() -> Component.literal("Everyone will now automatically connect to group '" + group.getName() + "' when joining"), false);
        return 1;
    }
}

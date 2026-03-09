package com.chenxi.scriptkiller.server.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.EnhancedGroupsManager;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.EnhancedGroupsVoicechatPlugin;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.EnhancedGroupsConfig;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.PersistentGroup;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.maxhenkel.voicechat.api.Group;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;

import java.util.List;
import java.util.UUID;

public class PersistentGroupCommands {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> voicechatNode) {
        voicechatNode.then(
                Commands.literal("persistentgroup")
                        .requires(source -> PermissionHelper.hasPermission(source, EnhancedGroupsConfig.PERSISTENT_GROUP_COMMAND_PERMISSION.get()))
                        .then(Commands.literal("add")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(ctx -> add(ctx, StringArgumentType.getString(ctx, "name"), Group.Type.NORMAL, false, null))
                                        .then(Commands.argument("type", GroupTypeArgument.groupType())
                                                .executes(ctx -> add(ctx, StringArgumentType.getString(ctx, "name"), GroupTypeArgument.getGroupType(ctx, "type"), false, null))
                                                .then(Commands.argument("hidden", BoolArgumentType.bool())
                                                        .executes(ctx -> add(ctx, StringArgumentType.getString(ctx, "name"), GroupTypeArgument.getGroupType(ctx, "type"), BoolArgumentType.getBool(ctx, "hidden"), null))
                                                        .then(Commands.argument("password", StringArgumentType.string())
                                                                .executes(ctx -> add(ctx, StringArgumentType.getString(ctx, "name"), GroupTypeArgument.getGroupType(ctx, "type"), BoolArgumentType.getBool(ctx, "hidden"), StringArgumentType.getString(ctx, "password")))
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(ctx -> removeByName(ctx, StringArgumentType.getString(ctx, "name")))
                                )
                                .then(Commands.argument("id", UuidArgument.uuid())
                                        .executes(ctx -> removeById(ctx, UuidArgument.getUuid(ctx, "id")))
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(PersistentGroupCommands::list)
                        )
        );
    }

    private static int add(CommandContext<CommandSourceStack> context, String name, Group.Type type, boolean hidden, String password) {
        if (name.isBlank()) {
            context.getSource().sendFailure(Component.literal("Name cannot be blank"));
            return 0;
        }

        if (EnhancedGroupsVoicechatPlugin.SERVER_API == null) {
            context.getSource().sendFailure(Component.literal("Voice chat not connected"));
            return 0;
        }

        Group vcGroup = EnhancedGroupsVoicechatPlugin.SERVER_API.groupBuilder()
                .setPersistent(true)
                .setName(name)
                .setPassword(password)
                .setType(type)
                .setHidden(hidden)
                .build();

        PersistentGroup persistentGroup = new PersistentGroup(name, password, PersistentGroup.Type.fromGroupType(type), hidden);
        EnhancedGroupsManager.PERSISTENT_GROUP_STORE.addGroup(persistentGroup);
        EnhancedGroupsManager.PERSISTENT_GROUP_STORE.addCached(vcGroup.getId(), persistentGroup);

        context.getSource().sendSuccess(() -> Component.literal("Successfully created persistent group " + name), false);
        return 1;
    }

    private static int removeByName(CommandContext<CommandSourceStack> context, String name) {
        PersistentGroup group = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(name);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }
        return removePersistentGroup(context, group);
    }

    private static int removeById(CommandContext<CommandSourceStack> context, UUID id) {
        PersistentGroup group = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroup(id);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }
        return removePersistentGroup(context, group);
    }

    private static int removePersistentGroup(CommandContext<CommandSourceStack> context, PersistentGroup persistentGroup) {
        if (EnhancedGroupsVoicechatPlugin.SERVER_API == null) {
            context.getSource().sendFailure(Component.literal("Voice chat not connected"));
            return 0;
        }

        UUID voicechatId = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getVoicechatId(persistentGroup.getId());

        if (voicechatId == null) {
            context.getSource().sendFailure(Component.literal("Group not found or not persistent"));
            return 0;
        }

        Group group = EnhancedGroupsVoicechatPlugin.SERVER_API.getGroup(voicechatId);

        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group not found"));
            return 0;
        }

        boolean removed = EnhancedGroupsVoicechatPlugin.SERVER_API.removeGroup(voicechatId);
        if (removed) {
            EnhancedGroupsManager.PERSISTENT_GROUP_STORE.removeGroup(persistentGroup);
            context.getSource().sendSuccess(() -> Component.literal("Removed group " + group.getName()), false);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Could not remove group " + group.getName()));
            return 0;
        }
    }

    private static int list(CommandContext<CommandSourceStack> context) {
        if (EnhancedGroupsVoicechatPlugin.SERVER_API == null) {
            context.getSource().sendFailure(Component.literal("Voice chat not connected"));
            return 0;
        }

        List<PersistentGroup> groups = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroups();

        if (groups.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("There are no persistent groups"), false);
            return 0;
        }

        for (PersistentGroup group : groups) {
            context.getSource().sendSuccess(() -> Component.literal(group.getName())
                            .append(" ")
                            .append(ComponentUtils.wrapInSquareBrackets(Component.literal("Remove")).withStyle(style ->
                                    style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nfa voicechat persistentgroup remove " + group.getId()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to remove group")))
                                            .applyFormat(ChatFormatting.GREEN)
                            ))
                            .append(" ")
                            .append(ComponentUtils.wrapInSquareBrackets(Component.literal("Auto Join")).withStyle(style ->
                                    style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nfa voicechat autojoingroup set " + group.getId()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to automatically connect to the group when joining")))
                                            .applyFormat(ChatFormatting.GREEN)
                            ))
                            .append(" ")
                            .append(ComponentUtils.wrapInSquareBrackets(Component.literal("Copy ID")).withStyle(style ->
                                    style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, group.getId().toString()))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy group ID")))
                                            .applyFormat(ChatFormatting.GREEN)
                            ))
                    , false);
        }
        return groups.size();
    }
}

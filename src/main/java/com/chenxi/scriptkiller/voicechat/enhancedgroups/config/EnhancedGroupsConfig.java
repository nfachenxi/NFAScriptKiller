package com.chenxi.scriptkiller.voicechat.enhancedgroups.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EnhancedGroupsConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue DEFAULT_INSTANT_GROUP_RANGE;
    public static final ModConfigSpec.ConfigValue<String> INSTANT_GROUP_NAME;
    public static final ModConfigSpec.EnumValue<PermissionType> INSTANT_GROUP_COMMAND_PERMISSION;
    public static final ModConfigSpec.EnumValue<PermissionType> PERSISTENT_GROUP_COMMAND_PERMISSION;
    public static final ModConfigSpec.EnumValue<PermissionType> AUTO_JOIN_GROUP_COMMAND_PERMISSION;
    public static final ModConfigSpec.EnumValue<PermissionType> AUTO_JOIN_GROUP_GLOBAL_COMMAND_PERMISSION;
    public static final ModConfigSpec.EnumValue<PermissionType> FORCE_JOIN_GROUP_COMMAND_PERMISSION;
    public static final ModConfigSpec.BooleanValue GROUP_SUMMARY;
    public static final ModConfigSpec.EnumValue<ForcedGroupType> FORCE_GROUP_TYPE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Enhanced Groups Configuration").push("enhanced_groups");

        DEFAULT_INSTANT_GROUP_RANGE = builder
                .comment("The default range for the /instantgroup command if no range was provided")
                .defineInRange("default_instant_group_range", 128.0, 1.0, Double.MAX_VALUE);

        INSTANT_GROUP_NAME = builder
                .comment("The name of the instant group")
                .define("instant_group_name", "Instant Group");

        INSTANT_GROUP_COMMAND_PERMISSION = builder
                .comment(
                        "The default permission level of the /instantgroup command",
                        "EVERYONE - Every player can use this command",
                        "OPS - Operators can use this command",
                        "NOONE - The command can't be used by anyone"
                )
                .defineEnum("instant_group_command_permission", PermissionType.EVERYONE);

        PERSISTENT_GROUP_COMMAND_PERMISSION = builder
                .comment(
                        "The default permission level of the /persistentgroup command",
                        "EVERYONE - Every player can use this command",
                        "OPS - Operators can use this command",
                        "NOONE - The command can't be used by anyone"
                )
                .defineEnum("persistent_group_command_permission", PermissionType.OPS);

        AUTO_JOIN_GROUP_COMMAND_PERMISSION = builder
                .comment(
                        "The default permission level of the /autojoingroup command",
                        "EVERYONE - Every player can use this command",
                        "OPS - Operators can use this command",
                        "NOONE - The command can't be used by anyone"
                )
                .defineEnum("auto_join_group_command_permission", PermissionType.EVERYONE);

        AUTO_JOIN_GROUP_GLOBAL_COMMAND_PERMISSION = builder
                .comment(
                        "The default permission level of the /autojoingroup global command",
                        "EVERYONE - Every player can use this command",
                        "OPS - Operators can use this command",
                        "NOONE - The command can't be used by anyone"
                )
                .defineEnum("auto_join_group_global_command_permission", PermissionType.OPS);

        FORCE_JOIN_GROUP_COMMAND_PERMISSION = builder
                .comment(
                        "The default permission level of the /forcejoingroup command",
                        "EVERYONE - Every player can use this command",
                        "OPS - Operators can use this command",
                        "NOONE - The command can't be used by anyone"
                )
                .defineEnum("force_join_group_command_permission", PermissionType.OPS);

        GROUP_SUMMARY = builder
                .comment("Determines if a summary of all groups should be shown when a player joins the server")
                .define("group_summary", true);

        FORCE_GROUP_TYPE = builder
                .comment(
                        "Forces all groups to be of the given type",
                        "OFF - No group type is forced",
                        "NORMAL - All groups are forced to be normal groups",
                        "OPEN - All groups are forced to be open groups",
                        "ISOLATED - All groups are forced to be isolated groups"
                )
                .defineEnum("force_group_type", ForcedGroupType.OFF);

        builder.pop();
        SPEC = builder.build();
    }
}

package com.chenxi.scriptkiller.server.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.PermissionType;
import net.minecraft.commands.CommandSourceStack;

public class PermissionHelper {

    public static boolean hasPermission(CommandSourceStack source, PermissionType permissionType) {
        return switch (permissionType) {
            case EVERYONE -> true;
            case OPS -> source.hasPermission(2);
            case NOONE -> false;
        };
    }
}

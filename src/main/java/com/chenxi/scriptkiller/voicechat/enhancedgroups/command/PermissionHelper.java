package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.voicechat.enhancedgroups.config.PermissionType;
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

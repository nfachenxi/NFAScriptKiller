package com.chenxi.scriptkiller.voicechat.enhancedgroups.config;

import de.maxhenkel.voicechat.api.Group;

import javax.annotation.Nullable;

public enum ForcedGroupType {
    OFF(null),
    NORMAL(Group.Type.NORMAL),
    OPEN(Group.Type.OPEN),
    ISOLATED(Group.Type.ISOLATED);

    @Nullable
    private final Group.Type type;

    ForcedGroupType(@Nullable Group.Type type) {
        this.type = type;
    }

    @Nullable
    public Group.Type getType() {
        return type;
    }

    public boolean isForced() {
        return type != null;
    }
}

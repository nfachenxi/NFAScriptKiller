package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class GroupTypeArgumentInfo implements ArgumentTypeInfo<GroupTypeArgument, GroupTypeArgumentInfo.Template> {

    @Override
    public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
        // No data to serialize
    }

    @Override
    public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
        return new Template();
    }

    @Override
    public void serializeToJson(Template template, JsonObject json) {
        // No data to serialize
    }

    @Override
    public Template unpack(GroupTypeArgument argument) {
        return new Template();
    }

    public static class Template implements ArgumentTypeInfo.Template<GroupTypeArgument> {
        @Override
        public GroupTypeArgument instantiate(CommandBuildContext context) {
            return GroupTypeArgument.groupType();
        }

        @Override
        public ArgumentTypeInfo<GroupTypeArgument, ?> type() {
            return ModArgumentTypes.GROUP_TYPE.get();
        }
    }
}

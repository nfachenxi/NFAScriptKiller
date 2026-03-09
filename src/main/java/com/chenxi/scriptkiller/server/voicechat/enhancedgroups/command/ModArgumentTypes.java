package com.chenxi.scriptkiller.server.voicechat.enhancedgroups.command;

import com.chenxi.scriptkiller.common.Constants;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArgumentTypes {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = 
            DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Constants.MODID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<GroupTypeArgument, ?>> GROUP_TYPE = 
            ARGUMENT_TYPES.register("group_type", () -> ArgumentTypeInfos.registerByClass(GroupTypeArgument.class, new GroupTypeArgumentInfo()));

    public static void register(IEventBus modEventBus) {
        ARGUMENT_TYPES.register(modEventBus);
    }
}

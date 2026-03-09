package com.chenxi.scriptkiller.voicechat.enhancedgroups.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class EnhancedGroupsCommands {

    public static void init() {
        NeoForge.EVENT_BUS.register(new EnhancedGroupsCommands());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        // 创建 /nfa voicechat 节点
        LiteralArgumentBuilder<CommandSourceStack> nfaNode = Commands.literal("nfa");
        LiteralArgumentBuilder<CommandSourceStack> voicechatNode = Commands.literal("voicechat");
        
        // 注册所有语音聊天相关命令到 voicechat 节点下
        InstantGroupCommands.register(voicechatNode);
        PersistentGroupCommands.register(voicechatNode);
        AutoJoinGroupCommands.register(voicechatNode);
        ForceJoinGroupCommands.register(voicechatNode);
        
        // 将 voicechat 节点添加到 nfa 节点下
        nfaNode.then(voicechatNode);
        
        // 注册 nfa 根命令
        dispatcher.register(nfaNode);
    }
}

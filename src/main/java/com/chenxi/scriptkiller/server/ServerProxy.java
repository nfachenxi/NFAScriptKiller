package com.chenxi.scriptkiller.server;

import com.chenxi.scriptkiller.common.Constants;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.EnhancedGroupsManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

/**
 * 服务端代理 - 处理所有服务端专属逻辑
 */
public class ServerProxy {

    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOGGER.info("Initializing Server Proxy");
        
        // 初始化语音聊天增强组模块
        EnhancedGroupsManager.init(modEventBus, modContainer);
    }
}

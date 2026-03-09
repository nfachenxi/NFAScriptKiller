package com.chenxi.scriptkiller.client;

import com.chenxi.scriptkiller.common.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

/**
 * 客户端代理 - 处理所有客户端专属逻辑
 */
public class ClientProxy {

    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOGGER.info("Initializing Client Proxy");
        
        // 未来在此注册客户端事件、渲染器、GUI等
    }
}

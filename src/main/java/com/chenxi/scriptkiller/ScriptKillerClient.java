package com.chenxi.scriptkiller;

import com.chenxi.scriptkiller.client.ClientProxy;
import com.chenxi.scriptkiller.common.Constants;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * 客户端入口类
 * 仅在客户端加载，处理客户端专属初始化
 */
@Mod(value = Constants.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ScriptKillerClient {
    
    public ScriptKillerClient(IEventBus modEventBus, ModContainer container) {
        // 注册配置界面
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        
        // 初始化客户端代理
        ClientProxy.init(modEventBus, container);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        Constants.LOGGER.info("ScriptKiller client setup");
        Constants.LOGGER.info("Player name: {}", Minecraft.getInstance().getUser().getName());
    }
}

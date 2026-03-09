package com.chenxi.scriptkiller;

import com.chenxi.scriptkiller.common.Constants;
import com.chenxi.scriptkiller.common.config.CommonConfig;
import com.chenxi.scriptkiller.server.ServerProxy;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * MC剧本杀模组主类
 * 负责模组的初始化和通用逻辑
 */
@Mod(Constants.MODID)
public class ScriptKiller {

    public ScriptKiller(IEventBus modEventBus, ModContainer modContainer) {
        // 注册通用设置事件
        modEventBus.addListener(this::commonSetup);

        // 注册服务端和游戏事件
        NeoForge.EVENT_BUS.register(this);

        // 注册通用配置
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        // 初始化服务端代理（包含语音聊天等服务端逻辑）
        ServerProxy.init(modEventBus, modContainer);

        Constants.LOGGER.info("ScriptKiller Mod initialized");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Constants.LOGGER.info("ScriptKiller common setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        Constants.LOGGER.info("ScriptKiller server starting");
    }
}

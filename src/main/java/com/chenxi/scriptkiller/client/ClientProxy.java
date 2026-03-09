package com.chenxi.scriptkiller.client;

import com.chenxi.scriptkiller.client.hud.CoordinateHudOverlay;
import com.chenxi.scriptkiller.client.keybinding.ModKeyBindings;
import com.chenxi.scriptkiller.common.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * 客户端代理 - 处理所有客户端专属逻辑
 */
public class ClientProxy {

    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOGGER.info("Initializing Client Proxy");
        
        // 注册MOD总线事件
        modEventBus.addListener(ClientProxy::registerGuiLayers);
        modEventBus.addListener(ClientProxy::registerKeyMappings);
        
        // 注册配置屏幕
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        // 注册坐标HUD，渲染在准星之上
        event.registerAbove(
                VanillaGuiLayers.CROSSHAIR,
                ResourceLocation.fromNamespaceAndPath(Constants.MODID, "coordinate_hud"),
                new CoordinateHudOverlay()
        );
    }

    @SubscribeEvent
    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        // 注册按键绑定
        event.register(ModKeyBindings.TOGGLE_COORDINATE_HUD);
    }
}

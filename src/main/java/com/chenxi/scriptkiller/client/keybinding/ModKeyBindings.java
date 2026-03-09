package com.chenxi.scriptkiller.client.keybinding;

import com.chenxi.scriptkiller.client.hud.CoordinateHudOverlay;
import com.chenxi.scriptkiller.common.Constants;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * 模组按键绑定
 */
public class ModKeyBindings {

    // 切换坐标HUD显示的按键（默认F8）
    public static final KeyMapping TOGGLE_COORDINATE_HUD = new KeyMapping(
            "key.scriptkiller.toggle_coordinate_hud",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            "key.categories.scriptkiller"
    );

    /**
     * 处理按键输入 - 游戏总线事件
     */
    @EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
    public static class KeyInputHandler {
        
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            while (TOGGLE_COORDINATE_HUD.consumeClick()) {
                CoordinateHudOverlay.toggle();
            }
        }
    }
}

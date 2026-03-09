package com.chenxi.scriptkiller.client.hud;

import com.chenxi.scriptkiller.common.config.CommonConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;

/**
 * 坐标HUD渲染器 - 在屏幕右下角显示玩家坐标
 */
public class CoordinateHudOverlay implements LayeredDraw.Layer {

    private static boolean enabled = true;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!enabled || !CommonConfig.SHOW_COORDINATE_HUD.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        
        if (player == null) {
            return;
        }

        // 获取玩家坐标（整数）
        int x = (int) player.getX();
        int y = (int) player.getY();
        int z = (int) player.getZ();

        // 格式化坐标文本
        String coordText = String.format("%d, %d, %d", x, y, z);

        // 获取屏幕尺寸
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 获取配置的边距
        int marginRight = CommonConfig.COORDINATE_HUD_MARGIN_RIGHT.get();
        int marginBottom = CommonConfig.COORDINATE_HUD_MARGIN_BOTTOM.get();

        // 计算文本宽度
        int textWidth = mc.font.width(coordText);

        // 计算渲染位置（右下角，留出边距）
        int posX = screenWidth - textWidth - marginRight;
        int posY = screenHeight - mc.font.lineHeight - marginBottom;

        // 渲染文本（灰色，不透明度增强：0xCCCCCCCC）
        guiGraphics.drawString(mc.font, coordText, posX, posY, 0xCCCCCCCC, false);
    }

    /**
     * 切换HUD显示状态
     */
    public static void toggle() {
        enabled = !enabled;
    }

    /**
     * 获取当前显示状态
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置显示状态
     */
    public static void setEnabled(boolean value) {
        enabled = value;
    }
}

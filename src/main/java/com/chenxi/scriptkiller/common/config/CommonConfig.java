package com.chenxi.scriptkiller.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 通用配置（双端共享）
 */
public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ========== 坐标HUD配置 ==========
    public static final ModConfigSpec.BooleanValue SHOW_COORDINATE_HUD;
    public static final ModConfigSpec.IntValue COORDINATE_HUD_MARGIN_RIGHT;
    public static final ModConfigSpec.IntValue COORDINATE_HUD_MARGIN_BOTTOM;

    static {
        BUILDER.push("coordinate_hud");
        
        SHOW_COORDINATE_HUD = BUILDER
                .comment("Enable coordinate HUD display")
                .translation("config.scriptkiller.show_coordinate_hud")
                .define("showCoordinateHud", true);
        
        COORDINATE_HUD_MARGIN_RIGHT = BUILDER
                .comment("Distance from right edge of screen (pixels)")
                .translation("config.scriptkiller.coordinate_hud_margin_right")
                .defineInRange("marginRight", 30, 0, 500);
        
        COORDINATE_HUD_MARGIN_BOTTOM = BUILDER
                .comment("Distance from bottom edge of screen (pixels)")
                .translation("config.scriptkiller.coordinate_hud_margin_bottom")
                .defineInRange("marginBottom", 30, 0, 500);
        
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}

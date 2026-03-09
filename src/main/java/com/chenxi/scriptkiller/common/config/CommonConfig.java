package com.chenxi.scriptkiller.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 通用配置（双端共享）
 */
public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 未来在此添加通用配置项

    public static final ModConfigSpec SPEC = BUILDER.build();
}

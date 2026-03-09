package com.chenxi.scriptkiller.server.voicechat.enhancedgroups;

import com.chenxi.scriptkiller.common.Constants;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.command.EnhancedGroupsCommands;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.command.ModArgumentTypes;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.AutoJoinGroupStore;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.EnhancedGroupsConfig;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.PersistentGroupStore;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.event.GroupSummaryEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.nio.file.Path;

public class EnhancedGroupsManager {

    public static PersistentGroupStore PERSISTENT_GROUP_STORE;
    public static AutoJoinGroupStore AUTO_JOIN_GROUP_STORE;

    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        // 注册配置
        modContainer.registerConfig(ModConfig.Type.SERVER, EnhancedGroupsConfig.SPEC, "scriptkiller/enhanced_groups.toml");

        // 注册自定义参数类型
        ModArgumentTypes.register(modEventBus);

        // 初始化存储
        Path configFolder = FMLPaths.CONFIGDIR.get().resolve("scriptkiller").resolve("enhanced_groups");
        PERSISTENT_GROUP_STORE = new PersistentGroupStore(configFolder.resolve("persistent-groups.json").toFile());
        AUTO_JOIN_GROUP_STORE = new AutoJoinGroupStore(configFolder.resolve("auto-join-groups.json").toFile());

        // 注册事件
        GroupSummaryEvents.init();
        EnhancedGroupsCommands.init();
        
        NeoForge.EVENT_BUS.addListener(EnhancedGroupsManager::onServerStopped);

        Constants.LOGGER.info("Enhanced Groups Manager initialized");
    }

    private static void onServerStopped(ServerStoppedEvent event) {
        PERSISTENT_GROUP_STORE.clearCache();
    }
}

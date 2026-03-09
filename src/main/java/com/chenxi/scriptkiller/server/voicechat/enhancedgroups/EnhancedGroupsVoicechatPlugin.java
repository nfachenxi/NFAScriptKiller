package com.chenxi.scriptkiller.server.voicechat.enhancedgroups;

import com.chenxi.scriptkiller.common.Constants;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.config.PersistentGroup;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.event.AutoJoinGroupEvents;
import com.chenxi.scriptkiller.server.voicechat.enhancedgroups.event.ForceGroupTypeEvents;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.CreateGroupEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

import javax.annotation.Nullable;
import java.util.List;

@ForgeVoicechatPlugin
public class EnhancedGroupsVoicechatPlugin implements VoicechatPlugin {

    @Nullable
    public static VoicechatServerApi SERVER_API;

    @Override
    public String getPluginId() {
        return Constants.MODID + "_enhanced_groups";
    }

    @Override
    public void initialize(VoicechatApi api) {
        Constants.LOGGER.info("Enhanced Groups plugin initialized");
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(PlayerConnectedEvent.class, AutoJoinGroupEvents::onPlayerConnected);
        registration.registerEvent(CreateGroupEvent.class, ForceGroupTypeEvents::onCreateGroup);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        SERVER_API = event.getVoicechat();

        List<PersistentGroup> groups = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getGroups();
        for (PersistentGroup group : groups) {
            Group vcGroup = SERVER_API.groupBuilder()
                    .setPersistent(true)
                    .setName(group.getName())
                    .setPassword(group.getPassword())
                    .setType(group.getType().getType())
                    .setHidden(group.isHidden())
                    .build();
            EnhancedGroupsManager.PERSISTENT_GROUP_STORE.addCached(vcGroup.getId(), group);
        }

        Constants.LOGGER.info("Added {} persistent groups", groups.size());
    }
}

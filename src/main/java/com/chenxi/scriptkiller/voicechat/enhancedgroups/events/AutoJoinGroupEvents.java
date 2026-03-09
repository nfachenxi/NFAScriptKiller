package com.chenxi.scriptkiller.voicechat.enhancedgroups.events;

import com.chenxi.scriptkiller.voicechat.enhancedgroups.EnhancedGroupsManager;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;

import java.util.UUID;

public class AutoJoinGroupEvents {

    public static void onPlayerConnected(PlayerConnectedEvent event) {
        VoicechatConnection connection = event.getConnection();
        UUID playerId = connection.getPlayer().getUuid();

        UUID globalGroupId = EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.getGlobalGroup();
        boolean globalGroupForced = EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.getGlobalGroupForced();
        UUID selectedGroupId;

        if (globalGroupForced) {
            selectedGroupId = globalGroupId;
        } else {
            UUID playerGroupId = EnhancedGroupsManager.AUTO_JOIN_GROUP_STORE.getPlayerGroup(playerId);
            selectedGroupId = (playerGroupId != null) ? playerGroupId : globalGroupId;
        }
        if (selectedGroupId == null) {
            return;
        }

        UUID voicechatId = EnhancedGroupsManager.PERSISTENT_GROUP_STORE.getVoicechatId(selectedGroupId);

        Group group = event.getVoicechat().getGroup(voicechatId);
        if (group == null) {
            return;
        }
        connection.setGroup(group);
    }
}

package com.zpedroo.voltzvips.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.PlayerVip;
import com.zpedroo.voltzvips.utils.config.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatMessageEvent event) {
        PlayerData data = DataManager.getInstance().load(event.getSender());
        PlayerVip selectedVIP = data.getSelectedVip();
        if (selectedVIP == null) return;
        if (!event.getTags().contains("vip")) return;

        event.setTagValue("vip", selectedVIP.getVip().getTag());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().load(player);
        if (data == null) return;

        for (PlayerVip vip : data.getVIPs()) {
            if (vip == null) continue;

            VipManager.getInstance().checkExpiration(player, vip);
        }

        PlayerVip selectedVip = data.getSelectedVip();
        if (selectedVip == null) return;
        if (!selectedVip.getVip().useJoinMessage()) return;

        for (String msg : Messages.JOIN_MESSAGE) {
            if (msg == null) continue;

            Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                    "{tag}",
                    "{player}"
            }, new String[]{
                    selectedVip.getVip().getTag(),
                    player.getName()
            }));
        }

        new HashSet<>(Bukkit.getOnlinePlayers()).forEach(target -> {
            target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5f, 10f);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().save(event.getPlayer());
    }
}
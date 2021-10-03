package com.zpedroo.voltzvips.hooks;

import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.objects.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private Plugin plugin;

    public PlaceholderAPIHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "vips";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        PlayerData data = DataManager.getInstance().load(player);
        return switch (identifier.toUpperCase()) {
            case "VIP" -> data.getSelectedVip() == null ? "-/-" : data.getSelectedVip().getVip().getTag();
            default -> null;
        };
    }
}
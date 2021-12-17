package com.zpedroo.voltzvips.managers.cache;

import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.Vip;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class DataCache {

    private Map<String, Vip> vips;
    private Map<OfflinePlayer, PlayerData> playerData;

    public DataCache() {
        this.vips = new HashMap<>(4);
        this.playerData = new HashMap<>(32);
    }

    public Map<String, Vip> getVips() {
        return vips;
    }

    public Map<OfflinePlayer, PlayerData> getPlayerData() {
        return playerData;
    }
}
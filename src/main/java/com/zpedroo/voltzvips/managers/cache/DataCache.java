package com.zpedroo.voltzvips.managers.cache;

import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.Vip;
import org.bukkit.entity.Player;

import java.util.*;

public class DataCache {

    private Map<String, Vip> vips;
    private Map<Player, PlayerData> playerData;

    public DataCache() {
        this.vips = new HashMap<>(4);
        this.playerData = new HashMap<>(128);
    }

    public Map<String, Vip> getVips() {
        return vips;
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }
}
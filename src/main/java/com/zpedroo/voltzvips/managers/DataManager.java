package com.zpedroo.voltzvips.managers;

import com.zpedroo.voltzvips.managers.cache.DataCache;
import com.zpedroo.voltzvips.mysql.DBConnection;
import com.zpedroo.voltzvips.objects.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private DataCache dataCache;

    public DataManager() {
        instance = this;
        this.dataCache = new DataCache();
    }

    public PlayerData load(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().loadData(player);
            cache(player, data);
        }

        return data;
    }

    public void save(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) return;
        if (!data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().saveData(data);
        data.setUpdate(false);
    }

    public void saveAll() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::save);
    }

    private void cache(Player player, PlayerData data) {
        dataCache.getPlayerData().put(player, data);
    }

    public DataCache getCache() {
        return dataCache;
    }
}
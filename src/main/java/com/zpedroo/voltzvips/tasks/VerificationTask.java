package com.zpedroo.voltzvips.tasks;

import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.PlayerVip;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;

import static com.zpedroo.voltzvips.utils.config.Settings.*;

public class VerificationTask extends BukkitRunnable {

    public VerificationTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * CHECK_INTERVAL, 20 * CHECK_INTERVAL);
    }

    @Override
    public void run() {
        new HashSet<>(DataManager.getInstance().getCache().getPlayerData().keySet()).forEach(player -> {
            PlayerData data = DataManager.getInstance().load(player);
            for (PlayerVip vip : data.getVIPs()) {
                if (vip == null) continue;

                VipManager.getInstance().checkExpiration(player, vip);
            }
        });
    }
}
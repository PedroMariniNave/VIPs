package com.zpedroo.voltzvips.tasks;

import com.zpedroo.voltzvips.managers.DataManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.voltzvips.utils.config.Settings.*;

public class SaveTask extends BukkitRunnable {

    public SaveTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * SAVE_INTERVAL, 20 * SAVE_INTERVAL);
    }

    @Override
    public void run() {
        DataManager.getInstance().saveAll();
    }
}
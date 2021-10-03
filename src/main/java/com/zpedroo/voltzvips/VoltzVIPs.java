package com.zpedroo.voltzvips;

import com.zpedroo.voltzvips.commands.VIPCmd;
import com.zpedroo.voltzvips.hooks.PlaceholderAPIHook;
import com.zpedroo.voltzvips.listeners.PlayerGeneralListeners;
import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.mysql.DBConnection;
import com.zpedroo.voltzvips.tasks.SaveTask;
import com.zpedroo.voltzvips.tasks.VerificationTask;
import com.zpedroo.voltzvips.utils.FileUtils;
import com.zpedroo.voltzvips.utils.formatter.DateFormatter;
import com.zpedroo.voltzvips.utils.formatter.TimeFormatter;
import com.zpedroo.voltzvips.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.voltzvips.utils.config.Settings.*;

public class VoltzVIPs extends JavaPlugin {

    private static VoltzVIPs instance;
    public static VoltzVIPs get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new DataManager();
        new VipManager();
        new TimeFormatter();
        new DateFormatter();
        new Menus();
        new VerificationTask(this);
        new SaveTask(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }

        registerCommand(COMMAND, ALIASES, new VIPCmd());
        registerListeners();
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAll();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private Boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}
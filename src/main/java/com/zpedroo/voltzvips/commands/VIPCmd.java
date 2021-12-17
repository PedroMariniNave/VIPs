package com.zpedroo.voltzvips.commands;

import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.PlayerVip;
import com.zpedroo.voltzvips.objects.Vip;
import com.zpedroo.voltzvips.utils.config.Messages;
import com.zpedroo.voltzvips.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class VIPCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;

        if (args.length > 0) {
            Player target = null;
            Vip vip = null;
            int durationInDays = 0;
            switch (args[0].toUpperCase()) {
                case "ADDALL":
                    if (!sender.hasPermission("vips.admin")) break;
                    if (args.length < 2) break;

                    try {
                        durationInDays = Integer.parseInt(args[1]);
                    } catch (Exception ex) {
                        // ignore
                    }

                    if (durationInDays <= 0) {
                        sender.sendMessage(Messages.INVALID_DURATION);
                        return true;
                    }

                    for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                        PlayerData data = DataManager.getInstance().load(offlinePlayer);
                        if (data.getVIPs().isEmpty()) continue;

                        for (PlayerVip playerVip : data.getVIPs()) {
                            playerVip.setExpiration(playerVip.getExpiration() + TimeUnit.DAYS.toMillis(durationInDays));
                        }

                        data.setUpdate(true);
                        DataManager.getInstance().save(offlinePlayer);
                    }
                    return true;
                case "GIVE":
                case "ADD":
                    if (!sender.hasPermission("vips.admin")) break;
                    if (args.length < 4) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Messages.OFFLINE_PLAYER);
                        return true;
                    }

                    vip = VipManager.getInstance().getVip(args[2]);
                    if (vip == null) {
                        sender.sendMessage(Messages.INVALID_VIP);
                        return true;
                    }

                    try {
                        durationInDays = Integer.parseInt(args[3]);
                    } catch (Exception ex) {
                        // ignore
                    }

                    if (durationInDays <= 0) {
                        sender.sendMessage(Messages.INVALID_DURATION);
                        return true;
                    }

                    VipManager.getInstance().addVip(target, vip, durationInDays);
                    return true;
                case "REMOVE":
                    if (!sender.hasPermission("vips.admin")) break;
                    if (args.length < 3) break;

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Messages.OFFLINE_PLAYER);
                        return true;
                    }

                    vip = VipManager.getInstance().getVip(args[2]);
                    if (vip == null) {
                        sender.sendMessage(Messages.INVALID_VIP);
                        return true;
                    }

                    VipManager.getInstance().removeVip(target, vip);
                    return true;
            }
        }

        if (player == null) return true;

        Menus.getInstance().openMainMenu(player);
        return false;
    }
}
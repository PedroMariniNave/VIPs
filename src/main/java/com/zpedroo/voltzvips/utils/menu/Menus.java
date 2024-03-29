package com.zpedroo.voltzvips.utils.menu;

import com.zpedroo.voltzvips.managers.DataManager;
import com.zpedroo.voltzvips.managers.VipManager;
import com.zpedroo.voltzvips.objects.PlayerVip;
import com.zpedroo.voltzvips.objects.Vip;
import com.zpedroo.voltzvips.utils.FileUtils;
import com.zpedroo.voltzvips.utils.builder.InventoryBuilder;
import com.zpedroo.voltzvips.utils.builder.InventoryUtils;
import com.zpedroo.voltzvips.utils.builder.ItemBuilder;
import com.zpedroo.voltzvips.utils.formatter.DateFormatter;
import com.zpedroo.voltzvips.utils.formatter.TimeFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");

            inventory.addItem(item, slot, () -> {
                if (StringUtils.contains(action, ":")) {
                    String[] split = action.split(":");
                    String command = split.length > 1 ? split[1] : null;
                    if (command == null) return;

                    switch (split[0].toUpperCase()) {
                        case "PLAYER":
                            player.chat("/" + command);
                            break;
                        case "CONSOLE":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(command, new String[]{
                                    "{player}"
                            }, new String[]{
                                    player.getName()
                            }));
                            break;
                    }
                }
            }, ActionType.ALL_CLICKS);
        }

        int vipSlot = FileUtils.get().getInt(file, "Inventory.vip-slot");
        ItemStack vipItem = null;

        PlayerVip selectedVIP = DataManager.getInstance().load(player).getSelectedVip();
        if (selectedVIP == null) {
            vipItem = ItemBuilder.build(FileUtils.get().getFile(file).get(), "No-VIP").build();
        } else {
            vipItem = selectedVIP.getVip().getDisplay().clone();
            ItemMeta meta = vipItem.getItemMeta();
            if (meta.hasDisplayName()) meta.setDisplayName(StringUtils.replaceEach(meta.getDisplayName(), new String[]{
                    "{remaining}",
                    "{expiration_date}"
            }, new String[]{
                    TimeFormatter.getInstance().format(selectedVIP.getExpiration() - System.currentTimeMillis()),
                    DateFormatter.getInstance().format(selectedVIP.getExpiration())
            }));

            ArrayList<String> lore = meta.hasLore() ? (ArrayList<String>) meta.getLore() : null;
            if (lore != null) {
                List<String> newLore = new ArrayList<>(lore.size());

                for (String line : lore) {
                    newLore.add(StringUtils.replaceEach(line, new String[] {
                            "{remaining}",
                            "{expiration_date}"
                    }, new String[] {
                            TimeFormatter.getInstance().format(selectedVIP.getExpiration() - System.currentTimeMillis()),
                            DateFormatter.getInstance().format(selectedVIP.getExpiration())
                    }));
                }

                meta.setLore(newLore);
            }

            vipItem.setItemMeta(meta);
        }

        inventory.addItem(vipItem, vipSlot, () -> {
            openChangeVIPMenu(player);
        }, ActionType.ALL_CLICKS);

        inventory.open(player);
    }

    public void openChangeVIPMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.CHANGE_VIP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            Vip vip = VipManager.getInstance().getVip(str);
            if (vip == null) continue;

            PlayerVip playerVIP = VipManager.getInstance().getPlayerVip(player, vip);
            boolean hasVIP = VipManager.getInstance().hasVip(player, vip);
            boolean usingVIP = VipManager.getInstance().usingVip(player, vip);

            String status = hasVIP ? (usingVIP ? "selected" : "select") : "no-has";

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{remaining}",
                    "{expiration_date}",
                    "{status}"
            }, new String[]{
                    playerVIP == null ? TimeFormatter.getInstance().format(0L) : TimeFormatter.getInstance().format(playerVIP.getExpiration() - System.currentTimeMillis()),
                    playerVIP == null ? DateFormatter.getInstance().format(0L) : DateFormatter.getInstance().format(playerVIP.getExpiration()),
                    ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Status-Lore." + status))
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");

            inventory.addItem(item, slot, () -> {
                if (!hasVIP || usingVIP) return;
                if (playerVIP == null) return;
                if (System.currentTimeMillis() >= playerVIP.getExpiration()) return;

                VipManager.getInstance().selectVip(player, playerVIP);
                openMainMenu(player);
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }
}
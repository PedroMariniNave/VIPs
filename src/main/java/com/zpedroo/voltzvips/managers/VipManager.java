package com.zpedroo.voltzvips.managers;

import com.zpedroo.voltzvips.VoltzVIPs;
import com.zpedroo.voltzvips.objects.PlayerData;
import com.zpedroo.voltzvips.objects.PlayerVip;
import com.zpedroo.voltzvips.objects.Vip;
import com.zpedroo.voltzvips.utils.FileUtils;
import com.zpedroo.voltzvips.utils.builder.ItemBuilder;
import com.zpedroo.voltzvips.utils.config.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class VipManager extends DataManager {

    private static VipManager instance;
    public static VipManager getInstance() { return instance; }

    public VipManager() {
        instance = this;
        this.loadVips();
    }

    public PlayerVip getPlayerVip(Player player, Vip vip) {
        Set<PlayerVip> vips = load(player).getVIPs();
        for (PlayerVip playerVip : vips) {
            if (playerVip == null) continue;
            if (!playerVip.getVip().getName().equals(vip.getName())) continue;

            return playerVip;
        }

        return null;
    }

    public boolean usingVip(Player player, Vip vip) {
        PlayerData data = load(player);
        PlayerVip selectedVip = data.getSelectedVip();
        if (selectedVip == null) return false;

        return selectedVip.getVip().getName().equals(vip.getName());
    }

    public boolean hasVip(Player player, Vip vip) {
        Set<PlayerVip> vips = load(player).getVIPs();
        for (PlayerVip playerVip : vips) {
            if (playerVip == null) continue;
            if (!playerVip.getVip().getName().equals(vip.getName())) continue;

            return true;
        }

        return false;
    }

    public void selectVip(Player player, PlayerVip playerVip) {
        if (!hasVip(player, playerVip.getVip())) return;
        PlayerData data = load(player);
        if (data == null) return;

        PlayerVip selectedVIP = data.getSelectedVip();
        if (selectedVIP != null) {
            VoltzVIPs.get().getServer().getScheduler().runTaskLater(VoltzVIPs.get(),
                    () -> executeCommands(player, selectedVIP.getVip().getRemoveCommands()), 0L);
        }

        data.setSelectedVip(playerVip);
        VoltzVIPs.get().getServer().getScheduler().runTaskLater(VoltzVIPs.get(),
                () -> executeCommands(player, playerVip.getVip().getAddCommands()), 10L);
    }

    public void addVip(Player player, Vip vip, int durationInDays) {
        PlayerData data = load(player);
        if (data == null) return;

        long durationInMillis = TimeUnit.DAYS.toMillis(durationInDays);

        PlayerVip playerVIP = null;
        if (hasVip(player, vip)) {
            playerVIP = getPlayerVip(player, vip);
            playerVIP.setExpiration(playerVIP.getExpiration() + durationInMillis);
        } else {
            playerVIP = new PlayerVip(vip, System.currentTimeMillis() + durationInMillis);
            data.addVip(playerVIP);
        }

        executeCommands(player, vip.getActivateCommands());
        selectVip(player, playerVIP);
    }

    public void removeVip(Player player, Vip vip) {
        PlayerData data = load(player);
        if (data == null) return;

        PlayerVip playerVip = getPlayerVip(player, vip);
        if (playerVip == null) return;

        data.removeVip(playerVip);
        executeCommands(player, vip.getRemoveCommands());
    }

    public void checkExpiration(Player player, PlayerVip playerVip) {
        if (System.currentTimeMillis() < playerVip.getExpiration()) return;

        PlayerData data = load(player);

        for (String msg : Messages.VIP_EXPIRED) {
            if (msg == null) continue;

            player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                    "{vip}"
            }, new String[]{
                    playerVip.getVip().getTag()
            }));
        }

        data.removeVip(playerVip);
        executeCommands(player, playerVip.getVip().getRemoveCommands());

        if (data.getSelectedVip().equals(playerVip)) {
            if (data.getVIPs().isEmpty()) {
                data.setSelectedVip(null);
                return;
            }

            PlayerVip newVip = data.getVIPs().stream().findFirst().get();
            data.setSelectedVip(newVip);
            executeCommands(player, newVip.getVip().getAddCommands());
        }
    }

    private void loadVips() {
        FileUtils.Files file = FileUtils.Files.CONFIG;
        for (String vipName : FileUtils.get().getSection(file, "VIPs")) {
            if (vipName == null) continue;

            String tag = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "VIPs." + vipName + ".tag"));
            ItemStack display = ItemBuilder.build(FileUtils.get().getFile(file).get(), "VIPs." + vipName + ".display").build();
            Boolean useJoinMessage = FileUtils.get().getBoolean(file, "VIPs." + vipName + ".use-join-message");
            List<String> activateCommands = FileUtils.get().getStringList(file, "VIPs." + vipName + ".commands.activate");
            List<String> removeCommands = FileUtils.get().getStringList(file, "VIPs." + vipName + ".commands.remove");
            List<String> addCommands = FileUtils.get().getStringList(file, "VIPs." + vipName + ".commands.add");

            cache(new Vip(vipName.toUpperCase(), tag, display, useJoinMessage, activateCommands, removeCommands, addCommands));
        }
    }

    public Vip getVip(String vipName) {
        return getCache().getVips().get(vipName.toUpperCase());
    }

    private void cache(Vip vip) {
        getCache().getVips().put(vip.getName(), vip);
    }

    private void executeCommands(Player player, List<String> commands) {
        for (String cmd : commands) {
            if (cmd == null) continue;

            VoltzVIPs.get().getServer().getScheduler().runTaskLater(VoltzVIPs.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(cmd, new String[]{
                    "{player}"
            }, new String[]{
                    player.getName()
            })), 0L);
        }
    }
}
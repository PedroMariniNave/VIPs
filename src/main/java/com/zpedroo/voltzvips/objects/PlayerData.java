package com.zpedroo.voltzvips.objects;

import java.util.Set;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private Set<PlayerVip> vips;
    private PlayerVip selectedVip;
    private Boolean update;

    public PlayerData(UUID uuid, Set<PlayerVip> vips, PlayerVip selectedVip) {
        this.uuid = uuid;
        this.vips = vips;
        this.selectedVip = selectedVip;
        this.update = false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Set<PlayerVip> getVIPs() {
        return vips;
    }

    public PlayerVip getSelectedVip() {
        return selectedVip;
    }

    public Boolean isQueueUpdate() {
        return update;
    }

    public void addVip(PlayerVip vip) {
        this.vips.add(vip);
        this.selectedVip = vip;
        this.update = true;
    }

    public void setSelectedVip(PlayerVip selectedVip) {
        this.selectedVip = selectedVip;
        this.update = true;
    }

    public void removeVip(PlayerVip vip) {
        this.vips.remove(vip);
        this.update = true;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }
}

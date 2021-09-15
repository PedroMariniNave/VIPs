package com.zpedroo.voltzvips.objects;

public class PlayerVip {

    private Vip vip;
    private Long expiration;

    public PlayerVip(Vip vip, Long expiration) {
        this.vip = vip;
        this.expiration = expiration;
    }

    public Vip getVip() {
        return vip;
    }

    public Long getExpiration() {
        return expiration;
    }
}
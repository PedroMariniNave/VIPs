package com.zpedroo.voltzvips.objects;

public class PlayerVip {

    private Vip vip;
    private long expiration;

    public PlayerVip(Vip vip, long expiration) {
        this.vip = vip;
        this.expiration = expiration;
    }

    public Vip getVip() {
        return vip;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
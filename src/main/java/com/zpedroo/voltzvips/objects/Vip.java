package com.zpedroo.voltzvips.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Vip {

    private String name;
    private String tag;
    private Boolean useJoinMessage;
    private ItemStack display;
    private List<String> activateCommands;
    private List<String> removeCommands;
    private List<String> addCommands;

    public Vip(String name, String tag, ItemStack display, Boolean useJoinMessage, List<String> activateCommands, List<String> removeCommands, List<String> addCommands) {
        this.name = name;
        this.tag = tag;
        this.useJoinMessage = useJoinMessage;
        this.display = display;
        this.activateCommands = activateCommands;
        this.removeCommands = removeCommands;
        this.addCommands = addCommands;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public Boolean useJoinMessage() {
        return useJoinMessage;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public List<String> getActivateCommands() {
        return activateCommands;
    }

    public List<String> getRemoveCommands() {
        return removeCommands;
    }

    public List<String> getAddCommands() {
        return addCommands;
    }
}
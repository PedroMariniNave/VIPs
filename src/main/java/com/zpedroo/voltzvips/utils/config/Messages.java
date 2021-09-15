package com.zpedroo.voltzvips.utils.config;

import com.zpedroo.voltzvips.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final List<String> VIP_EXPIRED = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.vip-expired"));

    public static final List<String> JOIN_MESSAGE = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.join-message"));

    public static final String INVALID_VIP = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-vip"));

    public static final String INVALID_DURATION = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-duration"));

    public static final String OFFLINE_PLAYER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String SECOND = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.second"));

    public static final String SECONDS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.seconds"));

    public static final String MINUTE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.minute"));

    public static final String MINUTES = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.minutes"));

    public static final String HOUR = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.hour"));

    public static final String HOURS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.hours"));

    public static final String DAY = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.day"));

    public static final String DAYS = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.days"));

    public static final String EXPIRED = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Time-Formatter.expired"));

    public static final String NEVER = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Date-Formatter.never"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>();
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}
package com.zpedroo.voltzvips.utils.config;

import com.zpedroo.voltzvips.utils.FileUtils;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final Long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final Long CHECK_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.check-interval");

    public static final String DATE_FORMAT = FileUtils.get().getString(FileUtils.Files.CONFIG, "Date-Formatter.format");
}
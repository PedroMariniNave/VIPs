package com.zpedroo.voltzvips.utils.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zpedroo.voltzvips.utils.config.Settings.*;
import static com.zpedroo.voltzvips.utils.config.Messages.*;

public class DateFormatter {

    private static DateFormatter instance;
    public static DateFormatter getInstance() { return instance; }

    public DateFormatter() {
        instance = this;
    }

    public String format(Long dateInMillis) {
        if (dateInMillis <= 0) return NEVER;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date(dateInMillis);

        return formatter.format(date);
    }
}
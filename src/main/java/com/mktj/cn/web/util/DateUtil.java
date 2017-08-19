package com.mktj.cn.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by lije09 on 2016/12/14.
 */
public class DateUtil {
    public static final DateTimeFormatter DATE_FORMAT_MM_YYYY = DateTimeFormatter.ofPattern("MMyyyy");
    public static final DateTimeFormatter DATE_FORMAT_MM_DD_YYYY = DateTimeFormatter.ofPattern("MMddyyyy");
    public static final DateTimeFormatter DATE_FORMAT_YYYY_MM = DateTimeFormatter.ofPattern("yyyyMM");

    private DateUtil() {
    }

    public static Date getYearBeginDate() {
        Calendar a = Calendar.getInstance();
        a.get(Calendar.YEAR);
        a.set(Calendar.DAY_OF_YEAR, 1);
        a.set(Calendar.HOUR_OF_DAY, 0);
        a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0);
        return a.getTime();
    }

    public static List<String> getYTDMonth() {
        List<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add("0" + i);
            } else {
                monthList.add(String.valueOf(i));
            }
        }
        return monthList;
    }
    public static String dateToString(Date date, DateFormat _format) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (_format != null) {
            format = _format;
        }
        return format.format(date);
    }


}

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
    private static final DateFormat format = new SimpleDateFormat("yyyyMMdd");
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
    public static String getTodayFormat() {
        return format.format(DateUtil.getCurrentDate());
    }

    public static Date getCurrentDate(){
        return new Date();
    }
}

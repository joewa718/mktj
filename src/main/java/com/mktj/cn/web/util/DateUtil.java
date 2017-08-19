package com.mktj.cn.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        Calendar a = Calendar.getInstance();
        int step = 0;
        while (true) {
            a.set(Calendar.MONTH, step);
            int curMonth = a.get(Calendar.MONTH) + 1;
            if (curMonth > 12) {
                break;
            }
            step++;
            if (curMonth < 10) {
                monthList.add("0" + curMonth);
            } else {
                monthList.add(String.valueOf(curMonth));
            }
        }
        return monthList;
    }

    public static void main(String[] args) {
        System.out.println(getYTDMonth());
    }

    public static String dateToString(Date date, DateFormat _format) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (_format != null) {
            format = _format;
        }
        return format.format(date);
    }


}

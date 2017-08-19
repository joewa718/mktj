package com.mktj.cn.web.util;

public class TimeUtil {

    public static void wait(int minute) {
        try {
            Thread.sleep(minute * 1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitSecond(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitMSecond(int msecond) {
        try {
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

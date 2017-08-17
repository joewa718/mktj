package com.mktj.cn.web.util;

public enum OrderStatus {

    待支付("待支付", 92), 已取消("已取消", 94), 已支付("已支付", 96), 已发货("已发货", 98), 已完成("已完成", 100);

    OrderStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int code;

    public static OrderStatus fromCode(int code) {
        if (code == 92) {
            return 待支付;
        }
        if (code == 94) {
            return 已取消;
        }
        if (code == 96) {
            return 已支付;
        }
        if (code == 98) {
            return 已发货;
        }
        if (code == 100) {
            return 已完成;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
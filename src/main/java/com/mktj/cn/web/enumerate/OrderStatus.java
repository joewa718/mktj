package com.mktj.cn.web.enumerate;

public enum OrderStatus {

    全部订单("全部订单", 100), 已取消("已取消", 90), 待支付("待支付", 92), 已支付("已支付", 94), 已发货("已发货", 96), 已完成("已完成", 98);

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
        if (code == 100) {
            return 全部订单;
        }
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
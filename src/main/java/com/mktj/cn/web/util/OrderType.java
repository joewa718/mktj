package com.mktj.cn.web.util;

public enum OrderType {
    普通订单("普通订单", 1), 服务订单("服务订单", 2);

    OrderType(String name, int code) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static OrderType fromCode(int code) {
        if (code == 1) {
            return 普通订单;
        }
        if (code == 2) {
            return 服务订单;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}
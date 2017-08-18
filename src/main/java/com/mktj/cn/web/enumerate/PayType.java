package com.mktj.cn.web.enumerate;

public enum PayType {
    线下转账("线下转账", 1), 余额支付("余额支付", 2);

    PayType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static PayType fromCode(int code) {
        if (code == 1) {
            return 线下转账;
        }
        if (code == 2) {
            return 余额支付;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}
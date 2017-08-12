package com.mktj.cn.web.util;

public enum ProductType {
    普通产品("普通产品",1),套餐产品("套餐产品",2);
    ProductType(String name, int code) {
        this.name =name;
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
    public static ProductType fromCode(int code) {
        if ( code == 1) {
            return 普通产品;
        }
        if ( code == 2) {
            return 套餐产品;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}
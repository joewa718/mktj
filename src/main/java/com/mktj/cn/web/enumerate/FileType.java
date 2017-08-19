package com.mktj.cn.web.enumerate;

public enum FileType {
    个人信息("个人信息", 1), 订单凭证("订单凭证", 2);

    FileType(String name, int code) {
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

    public static FileType fromCode(int code) {
        if (code == 1) {
            return 个人信息;
        }
        if (code == 2) {
            return 订单凭证;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }
}
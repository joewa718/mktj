package com.mktj.cn.web.util;

public enum RoleType {
    普通("普通", 0), 天使("天使", 1), 准合伙人("准合伙人", 2), 合伙人("合伙人", 3), 高级合伙人("高级合伙人", 4);

    RoleType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private int code;
    private String name;

    public static RoleType fromCode(int code) {
        if (code == 0) {
            return 普通;
        }
        if (code == 1) {
            return 天使;
        }
        if (code == 2) {
            return 准合伙人;
        }
        if (code == 3) {
            return 合伙人;
        }
        if (code == 4) {
            return 高级合伙人;
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!"
        );
    }


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

}
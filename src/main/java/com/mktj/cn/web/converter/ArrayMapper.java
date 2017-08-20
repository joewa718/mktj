package com.mktj.cn.web.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ArrayMapper {

    public String[] asArray(String str) {
        return str != null ? str.split(",") : null;
    }

    public String asString(String[] array) {
       return StringUtils.join(",");
    }
}
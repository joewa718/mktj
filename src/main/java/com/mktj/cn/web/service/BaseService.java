package com.mktj.cn.web.service;

import com.mktj.cn.web.po.User;
import com.mktj.cn.web.util.DateUtil;
import com.mktj.cn.web.util.GenerateRandomCode;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseService {

    protected String generateOrderCode(String userId) {
        GenerateRandomCode grc = new GenerateRandomCode();
        return grc.generateOrderCode(20, userId).toUpperCase();
    }

    protected String generateAuthCode() {
        GenerateRandomCode grc = new GenerateRandomCode();
        return grc.generate(17).toUpperCase();
    }

    protected String getLikeStr(User user) {
        if (StringUtils.isBlank(user.getOrgPath())) {
            return '>' + String.valueOf(user.getId()) + ">%";
        } else {
            return user.getOrgPath() + String.valueOf(user.getId()) + ">%";
        }
    }

    protected String getEqualStr(User user) {
        if (StringUtils.isBlank(user.getOrgPath())) {
            return '>' + String.valueOf(user.getId()) + ">";
        } else {
            return user.getOrgPath() + String.valueOf(user.getId()) + ">";
        }
    }

    protected   String bindOffSpringOrgPath(User higher, User lower) {
        return getOrgPath(higher) + (!StringUtils.isBlank(lower.getOrgPath()) ? lower.getOrgPath() : ">");
    }

    private  String getOrgPath(User user) {
        if (!StringUtils.isBlank(user.getOrgPath())) {
            return user.getOrgPath() + user.getId();
        } else {
            return '>' + String.valueOf(user.getId());
        }
    }
}

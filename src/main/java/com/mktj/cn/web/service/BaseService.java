package com.mktj.cn.web.service;

import com.mktj.cn.web.util.GenerateRandomCode;

public abstract class BaseService {

    protected String generateOrderCode(String userId) {
        GenerateRandomCode grc =new GenerateRandomCode();
        return grc.generateOrderCode(20,userId).toUpperCase();
    }

    protected String generateAuthCode() {
        GenerateRandomCode grc =new GenerateRandomCode();
        return grc.generate(17).toUpperCase();
    }


}

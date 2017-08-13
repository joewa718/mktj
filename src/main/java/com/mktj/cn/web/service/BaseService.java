package com.mktj.cn.web.service;

import com.mktj.cn.web.util.GenerateRandomCode;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public abstract class BaseService {

    protected String generateOrderCode(String userId) {
        GenerateRandomCode grc =new GenerateRandomCode(userId);
        return grc.generate(20).toUpperCase();
    }

    protected String generateAuthCode(String userId) {
        GenerateRandomCode grc =new GenerateRandomCode(userId);
        return grc.generate(15).toUpperCase();
    }


}

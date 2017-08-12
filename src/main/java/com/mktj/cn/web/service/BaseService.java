package com.mktj.cn.web.service;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;
import java.util.UUID;

public abstract class BaseService {

    protected String generateRandomCode(String phone) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong() + phone;
        String authorizationCode = new String(Hex.encodeHex(DigestUtils.md5(uuid)));
        return authorizationCode.toUpperCase();
    }

}

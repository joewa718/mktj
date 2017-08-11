package com.mktj.cn.web.service;

import com.mktj.cn.web.po.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class BaseService {

    protected String generateRandomCode(String phone) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong() + phone;
        String authorizationCode = new String(Hex.encodeHex(DigestUtils.md5(uuid)));
        return authorizationCode.toUpperCase();
    }

}

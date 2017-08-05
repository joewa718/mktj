package com.mktj.cn.util.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AESCryptPasswordEncoder implements PasswordEncoder {
    private final static Logger logger = LoggerFactory.getLogger(AESCryptPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            logger.warn("Empty raw password");
            return null;
        }
        return AESCryptUtil.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            logger.warn("Empty encoded password");
            return false;
        }

        String convertPassword = encode(rawPassword);

        if (convertPassword.equals(encodedPassword)) {
            return true;
        } else {
            logger.warn("Password match failed!");
            logger.warn("rawPassword:" + rawPassword);
            logger.warn("rawPassword encoded:" + convertPassword);
            logger.warn("encodedPassword:" + encodedPassword);
            return false;
        }
    }

}

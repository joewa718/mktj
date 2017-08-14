package com.mktj.cn.web.util;

import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SmsSender {

    private final static Logger log = LoggerFactory.getLogger(SmsSender.class);
    @Value("${sms.appId}")
    private int appId;
    @Value("${sms.appKey}")
    private String appKey;
    @Value("${sms.regTempId}")
    private int regTempId;

    private final static String time = "1";

    public void sendRegCode(String phone, String regCode) throws Exception {
        SmsSingleSender singleSender = new SmsSingleSender(appId, appKey);
        ArrayList<String> params = new ArrayList<>();
        params.add(regCode);
        params.add(time);
        SmsSingleSenderResult singleSenderResult = singleSender.sendWithParam("86", phone, regTempId, params, "", "", "");
        log.info(singleSenderResult.toString());
    }
}

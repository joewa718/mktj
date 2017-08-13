package com.mktj.cn.web.util;

import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;

import java.util.ArrayList;
public class SmsSDKDemo {
    public static void main(String[] args) {
    	try {
    		int appId = 1400038379;
    		String appKey = "1b9e33d5acd98689763038c5c347db41";
    		String phoneNumber1 = "18930983718";
    		int templateId = 33761;
	    	SmsSingleSender singleSender = new SmsSingleSender(appId, appKey);
	    	SmsSingleSenderResult singleSenderResult;
	    	ArrayList<String> params = new ArrayList<>();
	    	params.add("1234");
			params.add("1");
	    	singleSenderResult = singleSender.sendWithParam("86", phoneNumber1, templateId, params, "", "", "");
	    	System.out.println(singleSenderResult);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

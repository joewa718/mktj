package com.mktj.cn.web.controller;

import com.github.binarywang.wxpay.bean.WxPayOrderNotifyResponse;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mktj.cn.web.service.OrderService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/wechat/pay/")
public class WePayController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "wxPayService")
    private WxPayService wxPayService;
    @Autowired
    private OrderService orderService;
    @ResponseBody
    @RequestMapping(value = "/payNotice",method = RequestMethod.GET)
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.getOrderNotifyResult(xmlResult);
            String orderCode = result.getOutTradeNo();
            orderService.payWsSuccess(orderCode);
            return WxPayOrderNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayOrderNotifyResponse.fail(e.getMessage());
        }
    }
}

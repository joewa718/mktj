package com.mktj.cn.web.controller;

import com.github.binarywang.wxpay.bean.WxPayOrderNotifyResponse;
import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayBaseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/wechat/pay/")
public class WxPayController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "wxPayService")
    private WxPayService wxService;
    @ResponseBody
    @RequestMapping(value = "wxPay")
    public ResponseEntity<Object> pay(HttpServletRequest request, String orderNo, String subject) {
        try {
           /* Order order  = OrderService.findOne(orderNo);*/
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("主题");
            orderRequest.setOutTradeNo("订单号");
            orderRequest.setTotalFee(WxPayBaseRequest.yuanToFee(""));//元转成分
            orderRequest.setOpenid("openId");
            orderRequest.setSpbillCreateIp("userIp");
            orderRequest.setTimeStart("yyyyMMddHHmmss");
            orderRequest.setTimeExpire("yyyyMMddHHmmss");
            return new ResponseEntity<>(wxService.getPayInfo(orderRequest),HttpStatus.OK);
        } catch (Exception e) {
            logger.error("微信支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
            e.printStackTrace();
            return new ResponseEntity("支付失败，请稍后重试！",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping("/wx")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxService.getOrderNotifyResult(xmlResult);
            // 结果正确
            String orderId = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            String totalFee = WxPayBaseResult.feeToYuan(result.getTotalFee());
            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            return WxPayOrderNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayOrderNotifyResponse.fail(e.getMessage());
        }
    }
}

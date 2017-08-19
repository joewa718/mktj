package com.mktj.cn.web.vo;

import com.mktj.cn.web.enumerate.PayType;

/**
 * @author zhanwang
 * @create 2017-08-11 21:00
 **/
public class OrderVo {
    private long productId;
    private int productNum;
    private long deliverAddressId;
    private PayType payType;
    private String orderComment;
    private String recommendPhone;

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getDeliverAddressId() {
        return deliverAddressId;
    }

    public void setDeliverAddressId(long deliverAddressId) {
        this.deliverAddressId = deliverAddressId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getRecommendPhone() {
        return recommendPhone;
    }

    public void setRecommendPhone(String recommendPhone) {
        this.recommendPhone = recommendPhone;
    }
}

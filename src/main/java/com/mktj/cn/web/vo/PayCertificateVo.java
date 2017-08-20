package com.mktj.cn.web.vo;
/**
 * @author zhanwang
 * @create 2017-08-19 23:16
 **/
public class PayCertificateVo {
    private long orderId;
    private String[] payCertPhoto;

    public String[] getPayCertPhoto() {
        return payCertPhoto;
    }

    public void setPayCertPhoto(String[] payCertPhoto) {
        this.payCertPhoto = payCertPhoto;
    }

    private String payCertInfo;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public String getPayCertInfo() {
        return payCertInfo;
    }

    public void setPayCertInfo(String payCertInfo) {
        this.payCertInfo = payCertInfo;
    }
}

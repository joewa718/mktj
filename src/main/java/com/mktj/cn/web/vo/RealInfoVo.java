package com.mktj.cn.web.vo;

import java.io.Serializable;

public class RealInfoVo implements Serializable {
    private String idCard;
    private String realName;
    private String sex;
    private String birthday;
    private String province;
    private String idCardPhotoFront;
    private String idCardPhotoBack;
    private String city;
    private String region;
    private String occupation;
    private boolean isAudited;

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    public boolean isAudited() {
        return isAudited;
    }

    public void setAudited(boolean audited) {
        isAudited = audited;
    }

    public String getIdCard() {
        return idCard;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getIdCardPhotoFront() {
        return idCardPhotoFront;
    }

    public void setIdCardPhotoFront(String idCardPhotoFront) {
        this.idCardPhotoFront = idCardPhotoFront;
    }

    public String getIdCardPhotoBack() {
        return idCardPhotoBack;
    }

    public void setIdCardPhotoBack(String idCardPhotoBack) {
        this.idCardPhotoBack = idCardPhotoBack;
    }
}

package com.mktj.cn.web.dto;/**
 * Created by zhanwa01 on 2017/8/9.
 */

/**
 * @author zhanwang
 * @create 2017-08-09 11:18
 **/
public class RealInfoDTO {
    private String idCard;
    private String realName;
    private String sex;
    private String birthday;
    private String province;
    private String region;
    private String city;
    private String occupation;
    private String idCardPhotoFront;
    private String idCardPhotoBack;
    private boolean isAudited;

    public String getIdCard() {
        return idCard;
    }

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

    public boolean isAudited() {
        return isAudited;
    }

    public void setAudited(boolean audited) {
        isAudited = audited;
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
}

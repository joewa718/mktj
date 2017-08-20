package com.mktj.cn.web.po;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author zhanwang
 * @create 2017-08-08 14:38
 **/
@Entity
@Table(name = "t_deliver_address")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "deliveryMan")
    private String deliveryMan;
    @Column(name = "phone")
    private String phone;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "region")
    private String region;
    @Column(name = "detailed", length = 500)
    private String detailed;
    @Column(name = "is_default")
    private boolean isDefault;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryAddress that = (DeliveryAddress) o;

        if (id != that.id) return false;
        if (isDefault != that.isDefault) return false;
        if (deliveryMan != null ? !deliveryMan.equals(that.deliveryMan) : that.deliveryMan != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (province != null ? !province.equals(that.province) : that.province != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (detailed != null ? !detailed.equals(that.detailed) : that.detailed != null) return false;
        return user != null ? user.equals(that.user) : that.user == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (deliveryMan != null ? deliveryMan.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (detailed != null ? detailed.hashCode() : 0);
        result = 31 * result + (isDefault ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}

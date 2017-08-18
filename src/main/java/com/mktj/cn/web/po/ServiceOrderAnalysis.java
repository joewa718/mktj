package com.mktj.cn.web.po;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author zhanwang
 * @create 2017-08-16 20:02
 **/
@Entity
@Table(name = "t_service_order_analysis")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class ServiceOrderAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Transient
    private long total = 0;
    @Column(name = "un_confirm")
    private long unConfirm = 0;
    @Column(name = "un_pay")
    private long unPay = 0;
    @Column(name = "al_pay")
    private long alPay = 0;
    @Column(name = "al_send")
    private long alSend = 0;
    @Column(name = "al_complete")
    private long alComplete = 0;
    @Column(name = "al_cancel")
    private long alCancel = 0;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PostLoad
    public void calTotal() {
        this.total = this.unConfirm + this.unPay + this.alPay + this.alSend + this.alComplete +  + this.alCancel;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUnConfirm() {
        return unConfirm;
    }

    public void setUnConfirm(long unConfirm) {
        this.unConfirm = unConfirm;
    }

    public long getUnPay() {
        return unPay;
    }

    public void setUnPay(long unPay) {
        this.unPay = unPay;
    }

    public long getAlPay() {
        return alPay;
    }

    public void setAlPay(long alPay) {
        this.alPay = alPay;
    }

    public long getAlSend() {
        return alSend;
    }

    public void setAlSend(long alSend) {
        this.alSend = alSend;
    }

    public long getAlComplete() {
        return alComplete;
    }

    public void setAlComplete(long alComplete) {
        this.alComplete = alComplete;
    }

    public long getAlCancel() {
        return alCancel;
    }

    public void setAlCancel(long alCancel) {
        this.alCancel = alCancel;
    }
}

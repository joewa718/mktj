package com.mktj.cn.web.po;/**
 * Created by zhanwa01 on 2017/8/16.
 */

import javax.persistence.*;

/**
 * 团队组织分析
 * @author zhanwang
 * @create 2017-08-16 20:11
 **/
@Entity
@Table(name = "t_team_analysis")
public class TeamAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "angle")
    private long angle = 0;
    @Column(name = "quasiPartner")
    private long quasiPartner = 0;
    @Column(name = "partner")
    private long partner = 0;
    @Column(name = "senior_partner")
    private long seniorPartner = 0;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public long getAngle() {
        return angle;
    }

    public void setAngle(long angle) {
        this.angle = angle;
    }

    public long getQuasiPartner() {
        return quasiPartner;
    }

    public void setQuasiPartner(long quasiPartner) {
        this.quasiPartner = quasiPartner;
    }

    public long getPartner() {
        return partner;
    }

    public void setPartner(long partner) {
        this.partner = partner;
    }

    public long getSeniorPartner() {
        return seniorPartner;
    }

    public void setSeniorPartner(long seniorPartner) {
        this.seniorPartner = seniorPartner;
    }

}

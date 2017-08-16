package com.mktj.cn.web.po;

import javax.persistence.*;

/**
 * 团队组织架构
 * @author zhanwang
 * @create 2017-08-16 20:02
 **/
@Entity
@Table(name = "t_team_organization")
public class TeamOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "l_uid")
    private User lowerUser;

    @ManyToOne
    @JoinColumn(name = "h_uid")
    private User higherUser;

    public User getLowerUser() {
        return lowerUser;
    }

    public void setLowerUser(User lowerUser) {
        this.lowerUser = lowerUser;
    }

    public User getHigherUser() {
        return higherUser;
    }

    public void setHigherUser(User higherUser) {
        this.higherUser = higherUser;
    }

    @Column(name = "level") //级别
    private long level;

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

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
    @Column(name = "phone", nullable = false)
    private String teamCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamOrganization that = (TeamOrganization) o;

        if (!lowerUser.equals(that.lowerUser)) return false;
        return higherUser.equals(that.higherUser);
    }

    @Override
    public int hashCode() {
        int result = lowerUser.hashCode();
        result = 31 * result + higherUser.hashCode();
        return result;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

}

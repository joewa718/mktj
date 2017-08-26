package com.mktj.cn.web.po;

import javax.persistence.*;

/**
 * @author zhanwang
 * @create 2017-08-26 16:40
 **/
@Entity
@Table(name = "t_oauth_info")
public class OAuthInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "refreshToken", nullable = true)
    private String refreshToken;
    @Column(name = "accessToken", nullable = true)
    private String accessToken;
    @Column(name = "expires_in", nullable = true)
    private int expiresIn = -1;
    @Column(name = "open_id", nullable = true)
    private String openId;
    @Column(name = "scope", nullable = true)
    private String scope;
    @Column(name = "union_id", nullable = true)
    private String unionId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}

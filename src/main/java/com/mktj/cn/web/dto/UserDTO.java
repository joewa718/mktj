package com.mktj.cn.web.dto;

/**
 * Created by zhanwa01 on 2017/8/9.
 */
import java.math.BigDecimal;

/**
 * @author zhanwang
 * @create 2017-08-09 11:18
 **/
public class UserDTO {
    private String nickname;
    private String headPortrait;
    private String username;
    private String password;
    private String email;
    private String roleType;
    private Boolean isReceiveMessage;
    private String authorizationCode;
    private BigDecimal score;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isReceiveMessage() {
        return isReceiveMessage;
    }

    public void setReceiveMessage(Boolean receiveMessage) {
        isReceiveMessage = receiveMessage;
    }
}

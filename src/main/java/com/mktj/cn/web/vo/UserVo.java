package com.mktj.cn.web.vo;/**
 * Created by zhanwa01 on 2017/8/9.
 */

import com.mktj.cn.web.util.RoleType;

/**
 * @author zhanwang
 * @create 2017-08-09 11:18
 **/
public class UserVo {
    private String nickname;
    private String username;
    private String password;
    private String email;
    private RoleType roleType = RoleType.天使;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

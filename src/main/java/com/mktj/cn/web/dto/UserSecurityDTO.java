package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserSecurityDTO extends org.springframework.security.core.userdetails.User {
    private long id;
    private Boolean isWeUser;
    private String nickname;
    private String email;
    private Boolean disable;
    private String clientName;

    public UserSecurityDTO() {
        super(null, null, null);
    }

    public UserSecurityDTO(String phone, String password, Collection<? extends GrantedAuthority> authorities, User user)
            throws IllegalArgumentException {
        super(phone, password, authorities);
        if (user != null) {
            this.nickname = user.getNickname();
            this.id = user.getId();
            this.email = user.getEmail();
            this.disable = user.getDisable();
            this.isWeUser = user.getWeUser();
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getDisable() {
        return disable;
    }


    public void setDisable(Boolean disable) {
        this.disable = disable;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getWeUser() {
        return isWeUser;
    }

    public void setWeUser(Boolean weUser) {
        isWeUser = weUser;
    }

}

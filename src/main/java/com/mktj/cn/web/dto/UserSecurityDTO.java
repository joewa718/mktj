package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserSecurityDTO extends org.springframework.security.core.userdetails.User {
    private long id;
    private String phone;
    private String email;
    private Boolean disable;
    private String clientName;

    public UserSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, User user)
            throws IllegalArgumentException {
        super(username, password, authorities);
        if (user != null) {
            this.phone = user.getPhone();
            this.id = user.getId();
            this.email = user.getEmail();
            this.disable = user.getDisable();
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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}

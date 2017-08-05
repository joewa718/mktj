package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserSecurityDTO extends org.springframework.security.core.userdetails.User {
    private long id;
    private String name;
    private String opat;
    private String email;
    private Boolean disable;
    private String clientName;

    public UserSecurityDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, User user)
            throws IllegalArgumentException {
        super(username, password, authorities);
        if (user != null) {
            this.name = user.getName();
            this.id = user.getId();
            this.opat = user.getOpat();
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

    public String getOpat() {
        return opat;
    }

    public void setOpat(String opat) {
        this.opat = opat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}

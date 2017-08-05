package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.User;

public class LoginDTO {
    private long id;
    private String clientName;
    private String username;
    private String name;
    private String role = "1";

    public LoginDTO(User user) throws IllegalArgumentException {
        super();
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.username = user.getUsername();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

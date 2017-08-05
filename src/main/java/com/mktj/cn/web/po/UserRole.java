package com.mktj.cn.web.po;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_user_role")
@Cacheable
@org.hibernate.annotations.Cache(region = "entityCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "role_code", nullable = false)
    private long role_code;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public UserRole() {
        super();
    }

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

    public long getRole_code() {
        return role_code;
    }

    public void setRole_code(long role_code) {
        this.role_code = role_code;
    }

}

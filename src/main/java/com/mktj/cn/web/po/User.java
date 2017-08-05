package com.mktj.cn.web.po;

import com.mktj.cn.util.DateUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
@NamedEntityGraph(name = "UserAll",
        attributeNodes ={@NamedAttributeNode("client"),@NamedAttributeNode("userRoleSet")}
)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Column(name = "username", nullable = false, unique = true)
    private String username;


    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "opat")
    private Date opat;

    @Column(name = "email")
    private String email;

    @Column(name = "disable", nullable = false)
    private Boolean disable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRole> userRoleSet;


    public User() {
        super();
    }

    public User(long id) {
        super();
        this.id = id;
    }

    public User(String username, String name, String password) {
        super();
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public Set<UserRole> getUserRoleSet() {
        return userRoleSet;
    }

    public void setUserRoleSet(Set<UserRole> userRoleSet) {
        this.userRoleSet = userRoleSet;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpat() {
        return DateUtil.toFullDatetimeString(opat);
    }

    public void setOpat(Date opat) {
        this.opat = opat;
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


}

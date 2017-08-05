package com.mktj.cn.web.po;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_role")
@Cacheable
@org.hibernate.annotations.Cache(region = "entityCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "role_code", nullable = false, length = 10)
    private long roleCode;

    @NotEmpty
    @Column(name = "role_desc", nullable = false)
    private String roleDesc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(long roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}

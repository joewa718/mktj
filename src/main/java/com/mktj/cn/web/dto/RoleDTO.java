package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.Role;

public class RoleDTO {
    private long id;
    private long roleCode;
    private String roleDesc;

    public RoleDTO() {
    }

    public RoleDTO(Role r) {
        this.id = r.getId();
        this.roleCode = r.getRoleCode();
        this.roleDesc = r.getRoleDesc();
    }

    public static Role toRole(RoleDTO dto) {
        Role r = new Role();
        r.setId(dto.getId());
        r.setRoleCode(dto.getRoleCode());
        r.setRoleDesc(dto.getRoleDesc());
        return r;
    }

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

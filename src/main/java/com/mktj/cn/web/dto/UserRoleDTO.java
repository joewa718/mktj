package com.mktj.cn.web.dto;

import com.mktj.cn.web.po.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserRoleDTO {
    private long id;

    private long userid;

    private long role_code;

    public UserRoleDTO(UserRole todoIterator) {
        this.id = todoIterator.getId();
        this.userid = todoIterator.getUser().getId();
        this.role_code = todoIterator.getRole_code();
    }

    public static List<UserRoleDTO> convertList(List<UserRole> todo) {
        List<UserRoleDTO> listTodoDTO = new ArrayList<>();
        if (!todo.isEmpty()) {
            for (UserRole todoIterator : todo) {
                UserRoleDTO todoDTO = new UserRoleDTO(todoIterator);
                listTodoDTO.add(todoDTO);
            }
        }
        return listTodoDTO;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getRole_code() {
        return role_code;
    }

    public void setRole_code(long role_code) {
        this.role_code = role_code;
    }

}

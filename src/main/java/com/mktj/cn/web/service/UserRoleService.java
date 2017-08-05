package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.UserRoleDTO;

import java.util.List;

/**
 * Created by zhanwa01 on 2017/6/30.
 */
public interface UserRoleService {
    List<UserRoleDTO> getRolesByUser(long userid);
}

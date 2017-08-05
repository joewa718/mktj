package com.mktj.cn.web.service.imp;


import com.mktj.cn.web.po.User;
import com.mktj.cn.web.service.UserRoleService;
import com.mktj.cn.web.dto.UserRoleDTO;
import com.mktj.cn.web.po.UserRole;
import com.mktj.cn.web.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImp implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRoleDTO> getRolesByUser(long userid) {
        User user = new User();
        user.setId(userid);
        List<UserRole> userRoles = userRoleRepository.findAllByUser(user);
        List<UserRoleDTO> listUserRoleDTO = UserRoleDTO.convertList(userRoles);
        return listUserRoleDTO;
    }
}

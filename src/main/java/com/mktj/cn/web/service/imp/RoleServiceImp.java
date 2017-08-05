package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.RoleDTO;
import com.mktj.cn.web.service.RoleService;
import com.mktj.cn.web.po.Role;
import com.mktj.cn.web.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImp(@Autowired RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        Iterable<Role> roles = roleRepository.findAll();
        List<RoleDTO> list = new ArrayList<>();
        if (roles != null) {
            for (Role r : roles) {
                RoleDTO roleDTO = new RoleDTO(r);
                list.add(roleDTO);
            }
        }
        return list;
    }
}

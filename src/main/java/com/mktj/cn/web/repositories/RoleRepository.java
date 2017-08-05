package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    public Role findOne(Long id);

    public Role save(Role role);
}

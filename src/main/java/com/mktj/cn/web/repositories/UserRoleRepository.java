package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.User;
import com.mktj.cn.web.po.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    public UserRole findOne(Long id);

    @Query("select ur from UserRole ur where ur.user = ?1")
    List<UserRole> findAllByUser(User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from UserRole ur where ur.user = ?1")
    public int deleteByUser(User user);
}

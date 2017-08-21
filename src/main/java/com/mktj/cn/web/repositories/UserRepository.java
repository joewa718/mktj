package com.mktj.cn.web.repositories;

import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findOne(Long id);

    User save(User user);

    @Query("select u from User u where u.phone = ?1 and u.disable=0")
    User findByPhone(String phone);

    User getByAuthorizationCode(String authorizationCode);

    @Query("select u.roleType,count(u) from User u where u.orgPath like ?1 group by u.roleType")
    List<Object[]> analysisMemberDistribution(String orgPath);

    @Query("select u.roleType,count(u) from User u where u.orgPath = ?1 group by u.roleType")
    List<Object[]> analysisImmediateMemberDistribution(String orgPath);

    @Query("select u.roleType,count(u) from User u where  u.orgPath like ?1  group by u.roleType")
    List<Object[]> analysisNewMemberDistribution(String orgPath);

    @Query("select u from User u where u.roleType < ?1 and u.disable=0")
    List<User> findByLessThanRoleType(RoleType roleType);

    @Query("select count(u) from User u where u.orgPath like ?1 and u.disable=0")
    List<User> findByLikeOrgPath(String orgPath);
    @Query("select count(u) from User u where u.orgPath = ?1 and u.disable=0")
    List<User> findByOneLevelOrgPath(String orgPath);

    @Query(value = "select getChildList(?1)", nativeQuery = true)
    String processCalRoleType(long uid);
}

package com.mktj.cn.web.repositories;

import com.mktj.cn.web.enumerate.OrderStatus;
import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findOne(Long id);

    User save(User user);

    @Query("select u from User u where u.phone = ?1 and u.disable=0")
    User findByPhone(String phone);

    User getByAuthorizationCode(String authorizationCode);

    @Query("select u.roleType,count(u) from User u where u.orgPath like ?1  group by u.roleType")
    List<Object[]> analysisMemberDistribution(String orgPath);


    @Query("select u from User u where u.orgPath like ?1 and phone = ?2 group by u.roleType")
    User findOffspringCountByOrgPathAndPhone(String orgPath,String phone);


    @Query("select u.roleType,count(u) from User u where u.orgPath = ?1 group by u.roleType")
    List<Object[]> analysisImmediateMemberDistribution(String orgPath);

    @Query("select u.roleType,count(u) from User u where  u.orgPath like ?1 and diffDate > 30 group by u.roleType")
    List<Object[]> analysisNewMemberDistribution(String orgPath);

    @Query("select u from User u where u.roleType < ?1 and u.disable=0")
    List<User> findByLessThanRoleType(RoleType roleType);

    @Query("select u from User u where u.orgPath like ?1 and u.disable=0")
    List<User> findByLikeOrgPath(String orgPath);

    @Query("select u from User u where u.orgPath = ?1 and u.disable=0")
    List<User> findByOneLevelOrgPath(String orgPath);

    @Modifying
    @Query("update User o set o.orgPath = ?1  where o.orgPath like ?1 ")
    void updateOrgPathById(String orgPath,long id);

    @Modifying
    @Query("update User o set o.roleType = ?1 ,o.authorizationCode =?2 where o.id = ?3")
    void updateRoleTypeById(RoleType type,String authorizationCode, long id);

    @Query(value = "select ifnull(sum(if(u.role_type=4,4,1)),0) from t_user u where u.org_path like ?1 and u.role_type > 0 and u.disable=0", nativeQuery = true)
    Long findSumByLikeOrgPath(String orgPath);

    @Query(value = "select ifnull(sum(if(u.role_type=4,4,1)),0) from t_user u where u.org_path = ?1 and u.role_type > 0 and u.disable=0", nativeQuery = true)
    Long findSumByOneLevelOrgPath(String orgPath);

    @Query(value = "select getChildList(?1)", nativeQuery = true)
    String processCalRoleType(long uid);

    User findByAppId(String appId);
}

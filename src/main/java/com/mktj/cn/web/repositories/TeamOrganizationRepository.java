package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.TeamOrganization;
import com.mktj.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TeamOrganizationRepository extends CrudRepository<TeamOrganization, Long>, JpaSpecificationExecutor<TeamOrganization> {
    @Query("select t.lowerUser.roleType,count(t) from TeamOrganization t where t.teamCode =?1 group by t.lowerUser.roleType")
    List<Object[]> analysisMemberDistribution(String teamCode);

    @Query("select t.lowerUser.roleType,count(t) from TeamOrganization t where t.higherUser = ?1 and t.teamCode =?2 group by t.lowerUser.roleType")
    List<Object[]> analysisImmediateMemberDistribution(User user, String teamCode);

    @Query("select t.lowerUser.roleType,count(t) from TeamOrganization t where t.teamCode =?1 group by t.lowerUser.roleType")
    List<Object[]> analysisNewMemberDistribution(String teamCode);
}

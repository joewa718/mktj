package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.TeamOrganization;
import com.mktj.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamOrganizationRepository extends CrudRepository<TeamOrganization, Long>, JpaSpecificationExecutor<TeamOrganization> {

}

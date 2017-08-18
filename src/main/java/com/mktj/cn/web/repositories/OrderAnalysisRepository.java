package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.OrderAnalysis;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAnalysisRepository extends CrudRepository<OrderAnalysis, Long>, JpaSpecificationExecutor<OrderAnalysis> {
}

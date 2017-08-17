package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.OrderAnalysis;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAnalysisRepository extends CrudRepository<OrderAnalysis, Long>, JpaSpecificationExecutor<OrderAnalysis> {
}

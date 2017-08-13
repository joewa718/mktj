package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.Order;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("update Order o set o.orderStatus = ?1 where o.id = ?2 and o.user =?3")
    void updateOrderStatusByIdAndUser(OrderStatus status,long id,User user);

    List<Order> findByOrderTypeAndOrderStatusAndUser(OrderType orderType,OrderStatus orderStatus, User user);

    Order findOneByIdAndUser(long id,User user);

    Long countByOrderTypeAndUser(OrderType orderType, User user);
}

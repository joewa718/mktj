package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.Order;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.enumerate.OrderStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("update Order o set o.orderStatus = ?1 where o.id = ?2 and o.user =?3")
    void updateOrderStatusByIdAndUser(OrderStatus status, long id, User user);

    List<Order> findByOrderStatusAndUser(OrderStatus orderStatus, User user);

    Order findOneById(long id);

    Order findOneByOrderCode(String orderCode);

    @Query("select t.month,sum (t.productNum) from Order t where t.user =?1 and t.orderStatus >= ?2 and t.orderTime >= ?3 and t.orderTime <=?4 group by t.month")
    List<Object[]> analysisOrdinaryOrderSaleVolume(User user, OrderStatus orderStatus, Date begin, Date end);

    @Query("select t.month,sum (t.productNum) from Order t where t.id in ?1 and t.orderStatus >= ?2 and t.orderTime >= ?3 and t.orderTime <=?4  group by t.month")
    List<Object[]> analysisServiceOrderSaleVolume(List<Long> ids, OrderStatus orderStatus, Date begin, Date end);
}

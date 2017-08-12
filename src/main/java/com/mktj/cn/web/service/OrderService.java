package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.vo.OrderVo;

import java.util.List;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {

    void transactionOrder(String username,OrderVo orderVo);

    OrderDTO getOrder(String username, long orderId);

    void updateOrderStatusByIdAndUser(OrderStatus status, long id,  String username);

    List<OrderDTO> findByOrderStatusAndUser(OrderStatus status, String username);

    Long countByOrderTypeAndUser(String username,OrderType orderType);

}

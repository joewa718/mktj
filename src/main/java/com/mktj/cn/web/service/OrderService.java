package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.vo.OrderVo;

import java.util.List;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {

    void transactionOrder(String phone,OrderVo orderVo);

    OrderDTO getOrder(String phone, long orderId);

    void updateOrderStatusByIdAndUser(OrderStatus status, long id,  String phone);

    List<OrderDTO> findByOrderStatusAndUser(OrderStatus status, String phone);

    Long countByOrderTypeAndUser(String phone,OrderType orderType);

}

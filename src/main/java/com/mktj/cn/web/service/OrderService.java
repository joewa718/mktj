package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.vo.OrderVo;
import org.springframework.data.jpa.repository.Query;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {

    void transactionOrder(String phone, OrderVo orderVo) throws OperationNotSupportedException;

    OrderDTO getOrder(String phone, long orderId);

    void updateOrderStatusByIdAndUser(OrderStatus status, long id, String phone);

    List<OrderDTO> findByOrderTypeAndOrderStatusAndUser(OrderType orderType, OrderStatus status, String phone);

    Long countByOrderTypeAndUser(String phone, OrderType orderType);

    List<EntryDTO<String,Long>> groupOrderStatusCountByAndOrdinaryOrder(String phone);

    List<EntryDTO<String,Long>> groupOrderStatusCountByAndServiceOrder(String phone);

}

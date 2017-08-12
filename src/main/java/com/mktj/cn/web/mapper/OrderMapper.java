package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.po.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> orderToOrderDTOList(List<Order> orderList);

    List<OrderDTO> orderToOrderDTOList(Iterable<Order> orderList);
}
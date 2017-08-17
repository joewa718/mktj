package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.util.RoleType;
import com.mktj.cn.web.vo.OrderVo;
import org.springframework.data.jpa.repository.Query;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface OrderService {
    /**
     * 下单
     *
     * @param phone
     * @param orderVo
     * @return
     * @throws OperationNotSupportedException
     */
    OrderDTO applyOrder(String phone, OrderVo orderVo);

    /**
     * 支付订单
     *
     * @param phone
     * @param orderId
     * @return
     * @throws OperationNotSupportedException
     */
    OrderDTO payOrder(String phone, long orderId);

    /**
     * 获得订单详细信息
     *
     * @param phone
     * @param orderId
     * @return
     */
    OrderDTO getOrder(String phone, long orderId);

    /**
     * 获取对应状态下的订单列表
     *
     * @param orderType
     * @param status
     * @param phone
     * @return
     */
    List<OrderDTO> getOrderList(OrderType orderType, OrderStatus status, String phone);

    /**
     * 获取订单量用户量
     *
     * @param phone
     * @param orderType
     * @return
     */
    Integer getOrderCount(String phone, OrderType orderType);

    /**
     * 汇总订单量
     *
     * @param phone
     * @param orderType
     * @return
     */
    List<EntryDTO<String, Long>> summaryOrderCount(String phone, OrderType orderType);


    BigDecimal getProductPrice(RoleType roleType, Product product);
}

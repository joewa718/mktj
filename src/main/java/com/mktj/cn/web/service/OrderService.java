package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.po.Order;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.enumerate.OrderStatus;
import com.mktj.cn.web.enumerate.OrderType;
import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.vo.OrderVo;
import com.mktj.cn.web.vo.PayCertificateVo;

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
     * @param orderCode
     * @return
     * @throws OperationNotSupportedException
     */
    void payOrder(String orderCode);

    /**
     * 提交凭证信息
     * @param phone
     * @return
     */
    OrderDTO savePayCert(String phone, PayCertificateVo payCertificateVo);

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
     * @param phone
     * @param orderType
     * @return
     */
    Map<String, Long> summaryOrderCount(String phone, OrderType orderType);

    /**
     * 确认订单
     * @param phone
     * @param orderId
     * @return
     */
    OrderDTO sureOrder(String phone, long orderId);


    BigDecimal getProductPrice(RoleType roleType, Product product);

    void wePay(long orderId);
}

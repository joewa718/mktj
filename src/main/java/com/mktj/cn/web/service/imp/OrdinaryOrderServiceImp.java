package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.mapper.OrderMapper;
import com.mktj.cn.web.po.*;
import com.mktj.cn.web.repositories.DeliveryAddressRepository;
import com.mktj.cn.web.repositories.OrderRepository;
import com.mktj.cn.web.repositories.ProductRepository;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.service.BaseService;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.util.PayType;
import com.mktj.cn.web.util.RoleType;
import com.mktj.cn.web.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Qualifier("ordinaryOrderServiceImp")
@Service
public class OrdinaryOrderServiceImp extends  OrderServiceImp{
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional(value = "transactionManager")
    public Order transactionOrder(String phone, OrderVo orderVo,Product product) throws OperationNotSupportedException {
        User user = userRepository.findByPhone(phone);
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        if (deliveryAddress == null) {
            throw new OperationNotSupportedException("无法找到对应的收货地址");
        }
        if (orderVo.getProductNum() == 0) {
            throw new OperationNotSupportedException("购买订单量不能为 0 件");
        }
        int piece = orderVo.getProductNum();
        BigDecimal price = getProductPrice(user.getRoleType(), product);
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (orderVo.getPayType() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
        }
        Order order = saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        user.getOrderAnalysis().setUnPay(user.getOrderAnalysis().getAlPay() + 1);
        userRepository.save(user);
        return order;
    }

    public List<EntryDTO<String, Long>> groupOrderStatusCountByAndOrder(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Order> orderList = user.getOrderList();
        Map<Integer,Long> groupResult  = orderList.stream().collect( Collectors.groupingBy(order -> order.getOrderStatus().getCode(), Collectors.counting()));
        List<EntryDTO<String, Long>> result = new ArrayList<>();
        if (user.getOrderAnalysis() != null) {
            result.add(new EntryDTO<String, Long>(OrderStatus.待确认.getName(), groupResult.get(OrderStatus.待确认.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.待确认.getCode())));
            result.add(new EntryDTO<String, Long>(OrderStatus.待支付.getName(), groupResult.get(OrderStatus.待支付.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.待支付.getCode())));
            result.add(new EntryDTO<String, Long>(OrderStatus.已支付.getName(), groupResult.get(OrderStatus.已支付.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.已支付.getCode())));
            result.add(new EntryDTO<String, Long>(OrderStatus.已发货.getName(), groupResult.get(OrderStatus.已发货.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.已发货.getCode())));
            result.add(new EntryDTO<String, Long>(OrderStatus.已完成.getName(), groupResult.get(OrderStatus.已完成.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.已完成.getCode())));
            result.add(new EntryDTO<String, Long>(OrderStatus.已取消.getName(), groupResult.get(OrderStatus.已取消.getCode())==null ? Long.valueOf(0) : groupResult.get(OrderStatus.已取消.getCode())));
        }
        return result;
    }
}

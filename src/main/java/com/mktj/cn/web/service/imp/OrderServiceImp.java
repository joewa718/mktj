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
import com.mktj.cn.web.util.*;
import com.mktj.cn.web.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Qualifier("orderService")
@Service
public class OrderServiceImp extends BaseService implements OrderService{
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderMapper orderMapper;
    @Qualifier("ordinaryOrderService")
    @Autowired
    OrdinaryOrderServiceImp ordinaryOrderServiceImp;
    @Qualifier("serviceOrderService")
    @Autowired
    ServiceOrderServiceImp serviceOrderServiceImp;


    @Override
    @Transactional(value = "transactionManager")
    public void transactionOrder(String phone, OrderVo orderVo) throws OperationNotSupportedException {
        Product product = productRepository.findOne(orderVo.getProductId());
        if (product == null) {
            throw new OperationNotSupportedException("无法找到对应的商品");
        }
        if(product.getProductType() == ProductType.普通产品){
            ordinaryOrderServiceImp.transactionOrder(phone,orderVo,product);
        }else{
            serviceOrderServiceImp.transactionOrder(phone,orderVo,product);
        }
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    protected Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(generateOrderCode(String.valueOf(user.getId())));
        order.setUser(user);
        order.setOrderStatus(OrderStatus.待确认);
        order.setOrderTime(new Date());
        order.setOrderComment(orderVo.getOrderComment());
        order.setPayWay(orderVo.getPayType());
        order.setProductName(product.getProductName());
        order.setProductCode(product.getProductCode());
        order.setProductNum(piece);
        order.setProductPrice(price);
        order.setProductCost(totalCost);
        order.setReceiverProvince(deliveryAddress.getProvince());
        order.setReceiverCity(deliveryAddress.getCity());
        order.setReceiverRegion(deliveryAddress.getRegion());
        order.setReceiverDetailed(deliveryAddress.getDetailed());
        order.setReceiverPhone(deliveryAddress.getPhone());
        order.setReceiverName(deliveryAddress.getDeliveryMan());
        order.setSendName(product.getSendMan());
        order.setSendPhone(product.getSendPhone());
        order.setRecommendPhone(orderVo.getRecommendPhone());
        Optional<List<Order>> orderList = Optional.ofNullable(user.getOrderList());
        orderList.orElse(new ArrayList<>());
        orderList.get().add(order);
        order.setUser(user);
        return orderRepository.save(order);
    }

    @Override
    public OrderDTO getOrder(String phone, long orderId) {
        User user = userRepository.findByPhone(phone);
        Order order = orderRepository.findOneByIdAndUser(orderId, user);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    public void updateOrderStatusByIdAndUser(OrderStatus status, long id, String phone) {
        User user = userRepository.findByPhone(phone);
        orderRepository.updateOrderStatusByIdAndUser(status, id, user);
    }

    @Override
    public List<OrderDTO> findByOrderTypeAndOrderStatusAndUser(OrderType orderType, OrderStatus status, String phone) {
        User user = userRepository.findByPhone(phone);
        List<Order>  orderList = user.getOrderList().stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        return orderMapper.orderToOrderDTOList(orderList);
    }

    @Override
    public Long countByOrderTypeAndUser(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        return user.getOrderAnalysis().getTotal();
    }

    @Override
    public List<EntryDTO<String, Long>> groupOrderStatusCountByAndOrder(String phone,OrderType orderType) {
        if(orderType == OrderType.进货订单){
            return ordinaryOrderServiceImp.groupOrderStatusCountByAndOrder(phone);
        }else{
            return serviceOrderServiceImp.groupOrderStatusCountByAndOrder(phone);
        }
    }

    @Override
    public BigDecimal getProductPrice(RoleType roleType, Product product) {
        if (roleType == RoleType.天使 && product.getPrice1() != null) {
            return product.getPrice1();
        } else if (roleType == RoleType.准合伙人 && product.getPrice2() != null) {
            return product.getPrice2();
        } else if (roleType == RoleType.合伙人 && product.getPrice3() != null) {
            return product.getPrice3();
        } else if (roleType == RoleType.高级合伙人 && product.getPrice4() != null) {
            return product.getPrice4();
        }
        return product.getRetailPrice();
    }
}

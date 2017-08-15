package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.mapper.OrderMapper;
import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.po.Order;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.po.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends BaseService implements OrderService {
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderMapper orderMapper;

    @Transactional(value = "transactionManager")
    @Override
    public void transactionOrder(String phone, OrderVo orderVo) throws OperationNotSupportedException {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(orderVo.getProductId());
        if (product == null) {
            throw new OperationNotSupportedException("无法找到对应的商品");
        }
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        if (deliveryAddress == null) {
            throw new OperationNotSupportedException("无法找到对应的收货地址");
        }
        if (product.getRoleType() != null && product.getRoleType().getCode() > 0) {
            transactionPackageOrder(user, orderVo, deliveryAddress, product);
        } else {
            if (orderVo.getProductNum() == 0) {
                throw new OperationNotSupportedException("购买订单量不能为 0 件");
            }
            transactionOrdinaryOrder(user, orderVo, deliveryAddress, product);
        }
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private void transactionPackageOrder(User user, OrderVo orderVo, DeliveryAddress deliveryAddress, Product product) {
        int piece = product.getPiece();
        BigDecimal price = product.getRetailPrice();
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (orderVo.getPayType() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
        }
        saveOrder(user.getPhone(), orderVo, user, product, OrderType.服务订单, deliveryAddress, piece, price, totalCost);
        user.setRoleType(product.getRoleType());
        user.setAuthorizationCode(generateAuthCode());
        userRepository.save(user);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private void transactionOrdinaryOrder(User user, OrderVo orderVo, DeliveryAddress deliveryAddress, Product product) {
        int piece = orderVo.getProductNum();
        BigDecimal price = getProductPrice(user.getRoleType(), product);
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (orderVo.getPayType() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
        }
        saveOrder(user.getPhone(), orderVo, user, product, OrderType.普通订单, deliveryAddress, piece, price, totalCost);
        userRepository.save(user);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private void saveOrder(String phone, OrderVo orderVo, User user, Product product, OrderType orderType, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(generateOrderCode(String.valueOf(user.getId())));
        order.setUser(user);
        order.setOrderStatus(OrderStatus.待确认);
        order.setOrderTime(new Date());
        order.setOrderComment(orderVo.getOrderComment());
        order.setOrderType(orderType);
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
        Optional<List<Order>> orderList = Optional.ofNullable(user.getOrderList());
        orderList.orElse(new ArrayList<>());
        orderList.get().add(order);
        order.setUser(user);
        orderRepository.save(order);
    }

    private BigDecimal getProductPrice(RoleType roleType, Product product) {
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
        List<Order> orderList = orderRepository.findByOrderTypeAndOrderStatusAndUser(orderType, status, user);
        return orderMapper.orderToOrderDTOList(orderList);
    }

    @Override
    public Long countByOrderTypeAndUser(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        Long count = orderRepository.countByOrderTypeAndUser(orderType, user);
        return count;
    }
}

package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.enumerate.*;
import com.mktj.cn.web.mapper.OrderMapper;
import com.mktj.cn.web.po.*;
import com.mktj.cn.web.repositories.*;
import com.mktj.cn.web.service.BaseService;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends BaseService implements OrderService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    TeamOrganizationRepository teamOrganizationRepository;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO applyOrder(String phone, OrderVo orderVo) {
        Product product = productRepository.findOne(orderVo.getProductId());
        if (product == null) {
            throw new RuntimeException("无法找到对应的商品");
        }
        User user = userRepository.findByPhone(phone);
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        if (deliveryAddress == null) {
            throw new RuntimeException("无法找到对应的收货地址");
        }
        int piece;
        BigDecimal price;
        if (product.getProductType() == ProductType.套餐产品) {
            piece = product.getPiece();
            price = product.getRetailPrice();
        } else {
            piece = orderVo.getProductNum();
            price = getProductPrice(user.getRoleType(), product);
        }
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        //新增订单信息
        Order order = saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO payOrder(String phone, long orderId) {
        User user = userRepository.findByPhone(phone);
        Order order = orderRepository.findOne(orderId);
        int piece = order.getProductNum();
        BigDecimal price = order.getProductPrice();
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (order.getPayWay() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
            order.setOrderStatus(OrderStatus.已支付);
            orderRepository.updateOrderStatusByIdAndUser(OrderStatus.已支付, orderId, user);
        }else{//线下转账，由属确认支付
            if(order.getRecommendPhone() == null || !user.getPhone().equals(order.getRecommendPhone())){
                throw new RuntimeException("对不起，线下转账为成功，需要推荐人确认支付");
            }
            order.setOrderStatus(OrderStatus.已支付);
            orderRepository.updateOrderStatusByIdAndUser(OrderStatus.已支付, orderId, order.getUser());
        }
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        if (product == null) {
            throw new RuntimeException("无法找到对应的商品");
        }
        if (product.getProductType() == ProductType.套餐产品) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(generateAuthCode());
                userRepository.save(user);
            }
            if (product.getRoleType().getCode() > user.getRoleType().getCode()) {
                user.setRoleType(product.getRoleType());
                userRepository.save(user);
            }
            //更新推荐人服务订单
            if (!StringUtils.isBlank(order.getRecommendPhone())) {
                User recommend_man = userRepository.findByPhone(order.getRecommendPhone());
                if (recommend_man != null) {
                    order.getHigherUserList().add(recommend_man);
                    recommend_man.getServiceOrderList().add(order);
                    userRepository.save(recommend_man);
                    joinTeam(recommend_man, user);
                }
            }
        }
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void joinTeam(User recommend_man, User user) {
        Set<TeamOrganization> higherUserSet = recommend_man.getHigherUserList();
        if (higherUserSet.size() > 0) {
            higherUserSet.forEach(team -> {
                TeamOrganization teamOrganization = new TeamOrganization();
                teamOrganization.setHigherUser(recommend_man);
                teamOrganization.setLowerUser(user);
                teamOrganization.setTeamCode(team.getTeamCode());
                teamOrganizationRepository.save(teamOrganization);
            });
        } else {
            TeamOrganization teamOrganization = new TeamOrganization();
            teamOrganization.setLowerUser(user);
            teamOrganization.setHigherUser(recommend_man);
            teamOrganization.setTeamCode(recommend_man.getPhone());
            teamOrganizationRepository.save(teamOrganization);
        }

    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(generateOrderCode(String.valueOf(user.getId())));
        order.setUser(user);
        order.setOrderStatus(OrderStatus.待支付);
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
        Order order = orderRepository.findOneById(orderId);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    public Integer getOrderCount(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        List<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        return orderList.size();
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

    @Override
    public List<OrderDTO> getOrderList(OrderType orderType, OrderStatus status, String phone) {
        User user = userRepository.findByPhone(phone);
        List<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        if (status != OrderStatus.全部订单) {
            orderList = orderList.stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        }
        List<OrderDTO> orderDTOList = orderMapper.orderToOrderDTOList(orderList);
        orderDTOList.forEach(orderDTO -> orderDTO.setOrderType(orderType.getName()));
        return orderDTOList;
    }

    @Override
    public Map<String, Long> summaryOrderCount(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        List<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        Map<Integer, Long> groupResult = orderList.stream().collect(Collectors.groupingBy(order -> order.getOrderStatus().getCode(), Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        if (user.getOrderAnalysis() != null) {
            long unPay = groupResult.get(OrderStatus.待支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.待支付.getCode());
            long alPay = groupResult.get(OrderStatus.已支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已支付.getCode());
            long alSend = groupResult.get(OrderStatus.已发货.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已发货.getCode());
            long complete = groupResult.get(OrderStatus.已完成.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已完成.getCode());
            long cancel = groupResult.get(OrderStatus.已取消.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已取消.getCode());
            map.put("全部订单", Long.valueOf(unPay + alPay + alSend + complete + cancel));
            map.put(OrderStatus.待支付.getName(), unPay);
            map.put(OrderStatus.已支付.getName(), alPay);
            map.put(OrderStatus.已发货.getName(), alSend);
            map.put(OrderStatus.已完成.getName(), complete);
            map.put(OrderStatus.已取消.getName(), cancel);
        }
        return map;
    }
}

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
        User sendUser = userRepository.findByPhone(orderVo.getRecommendPhone());
        if (sendUser == null) {
            throw new RuntimeException("您填写的推荐人手机号码不存在");
        }
        if (product.getRoleType() != null && product.getRoleType().getCode() > 0) {
            transactionPackageOrder(sendUser, user, orderVo, deliveryAddress, product);
        } else {
            if (orderVo.getProductNum() == 0) {
                throw new OperationNotSupportedException("购买订单量不能为 0 件");
            }
            transactionOrdinaryOrder(user, orderVo, deliveryAddress, product);
        }
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
        saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        user.getOrderAnalysis().setUnPay(user.getOrderAnalysis().getAlPay() + 1);
        userRepository.save(user);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private void transactionPackageOrder(User sendUser, User user, OrderVo orderVo, DeliveryAddress deliveryAddress, Product product) {
        int piece = product.getPiece();
        BigDecimal price = product.getRetailPrice();
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (orderVo.getPayType() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
        }
        Order order = saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        user.setRoleType(product.getRoleType());
        user.setAuthorizationCode(generateAuthCode());
        user.getOrderAnalysis().setUnPay(user.getOrderAnalysis().getAlPay() + 1);
        userRepository.save(user);
        updateHigherLevel(sendUser, user, product, order, 0);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
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

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void updateHigherLevel(User sendUser, User user, Product product, Order order, int level) {
        if (level == 3) {
            return;
        }
        User curUser = sendUser;
        if (sendUser == null) {
            curUser = user;
        }
        curUser.getServiceOrderAnalysis().setUnPay(user.getServiceOrderAnalysis().getAlPay() + 1);
        if (product.getRoleType().getCode() > user.getRoleType().getCode()) {
            calTeamAnalysis(user, product, curUser);
        }
        List<TeamOrganization> teamOrganizationList = curUser.getLowerList().stream().filter(teamOrganization -> teamOrganization.getLowerUser().getId() == user.getId()).collect(Collectors.toList());
        if (teamOrganizationList.size() == 0) {
            TeamOrganization teamOrganization = new TeamOrganization();
            teamOrganization.setHigherUser(curUser);
            teamOrganization.setLowerUser(user);
            curUser.getLowerList().add(teamOrganization);
        }
        curUser.getServiceOrderList().add(order);
        order.getHigherUserList().add(curUser);
        userRepository.save(curUser);
        List<TeamOrganization> userList = curUser.getHigherUserList();
        for (TeamOrganization higherUser : userList) {
            updateHigherLevel(higherUser.getHigherUser(), user, product, order, ++level);
        }
    }

    /**
     * 计算团队人员成分
     *
     * @param user
     * @param product
     * @param sendUser
     */
    private void calTeamAnalysis(User user, Product product, User sendUser) {
        if (user.getRoleType() == RoleType.天使) {
            sendUser.getTeamAnalysis().setAngle(sendUser.getTeamAnalysis().getAngle() - 1);
        }

        if (user.getRoleType() == RoleType.合伙人) {
            sendUser.getTeamAnalysis().setPartner(sendUser.getTeamAnalysis().getPartner() - 1);
        }

        if (user.getRoleType() == RoleType.准合伙人) {
            sendUser.getTeamAnalysis().setQuasiPartner(sendUser.getTeamAnalysis().getQuasiPartner() - 1);
        }

        if (user.getRoleType() == RoleType.高级合伙人) {
            sendUser.getTeamAnalysis().setSeniorPartner(sendUser.getTeamAnalysis().getSeniorPartner() - 1);
        }

        if (product.getRoleType() == RoleType.天使) {
            sendUser.getTeamAnalysis().setAngle(sendUser.getTeamAnalysis().getAngle() + 1);
        }

        if (product.getRoleType() == RoleType.合伙人) {
            sendUser.getTeamAnalysis().setPartner(sendUser.getTeamAnalysis().getPartner() + 1);
        }

        if (product.getRoleType() == RoleType.准合伙人) {
            sendUser.getTeamAnalysis().setQuasiPartner(sendUser.getTeamAnalysis().getQuasiPartner() + 1);
        }

        if (product.getRoleType() == RoleType.高级合伙人) {
            sendUser.getTeamAnalysis().setSeniorPartner(sendUser.getTeamAnalysis().getSeniorPartner() + 1);
        }
    }


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
        List<Order> orderList = null;
        if (OrderType.进货订单 == orderType) {
            orderList = user.getOrderList().stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        } else {
            orderList = user.getServiceOrderList().stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        }
        return orderMapper.orderToOrderDTOList(orderList);
    }

    @Override
    public Long countByOrderTypeAndUser(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        return user.getOrderAnalysis().getTotal();
    }

    @Override
    public List<EntryDTO<String, Long>> groupOrderStatusCountByAndOrdinaryOrder(String phone) {
        User user = userRepository.findByPhone(phone);
        List<EntryDTO<String, Long>> result = new ArrayList<>();
        if (user.getOrderAnalysis() != null) {
            result.add(new EntryDTO<>(OrderStatus.待确认.getName(), user.getOrderAnalysis().getUnConfirm()));
            result.add(new EntryDTO<>(OrderStatus.待支付.getName(), user.getOrderAnalysis().getUnPay()));
            result.add(new EntryDTO<>(OrderStatus.已支付.getName(), user.getOrderAnalysis().getAlPay()));
            result.add(new EntryDTO<>(OrderStatus.已发货.getName(), user.getOrderAnalysis().getAlSend()));
            result.add(new EntryDTO<>(OrderStatus.已完成.getName(), user.getOrderAnalysis().getAlComplete()));
            result.add(new EntryDTO<>(OrderStatus.已取消.getName(), user.getOrderAnalysis().getAlCancel()));
        }
        return result;
    }

    @Override
    public List<EntryDTO<String, Long>> groupOrderStatusCountByAndServiceOrder(String phone) {
        User user = userRepository.findByPhone(phone);
        List<EntryDTO<String, Long>> result = new ArrayList<>();
        if (user.getOrderAnalysis() != null) {
            result.add(new EntryDTO<>(OrderStatus.待确认.getName(), user.getServiceOrderAnalysis().getUnConfirm()));
            result.add(new EntryDTO<>(OrderStatus.待支付.getName(), user.getServiceOrderAnalysis().getUnPay()));
            result.add(new EntryDTO<>(OrderStatus.已支付.getName(), user.getServiceOrderAnalysis().getAlPay()));
            result.add(new EntryDTO<>(OrderStatus.已发货.getName(), user.getServiceOrderAnalysis().getAlSend()));
            result.add(new EntryDTO<>(OrderStatus.已完成.getName(), user.getServiceOrderAnalysis().getAlComplete()));
            result.add(new EntryDTO<>(OrderStatus.已取消.getName(), user.getServiceOrderAnalysis().getAlCancel()));
        }
        return result;
    }
}

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
public class ServiceOrderServiceImp extends OrdinaryOrderServiceImp{
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

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
        User recommend_man = userRepository.findByPhone(orderVo.getRecommendPhone());
        if (recommend_man == null) {
            throw new RuntimeException("您填写的推荐人手机号码不存在");
        }
        int piece = product.getPiece();
        BigDecimal price = product.getRetailPrice();
        BigDecimal totalCost = price.multiply(BigDecimal.valueOf(piece));
        if (orderVo.getPayType() == PayType.余额支付) {
            if (totalCost.compareTo(user.getScore()) == 1) {
                throw new RuntimeException("对不起，您的积分不足，无法购买");
            }
            user.setScore(user.getScore().subtract(totalCost));
        }
        //更新订单信息
        Order order = super.saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        user.setRoleType(product.getRoleType());
        user.setAuthorizationCode(generateAuthCode());
        user.getOrderAnalysis().setUnPay(user.getOrderAnalysis().getAlPay() + 1);
        userRepository.save(user);
        //更新推荐人服务订单
        order.getHigherUserList().add(recommend_man);
        recommend_man.getServiceOrderAnalysis().setUnPay(recommend_man.getServiceOrderAnalysis().getUnPay() + 1);
        recommend_man.getServiceOrderList().add(order);
        userRepository.save(recommend_man);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void updateHigherLevel(User recommend_man, User user, Product product, Order order, int level) {
        if (level == 3) {
            return;
        }
        User curUser = recommend_man;
        if (recommend_man == null) {
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
            teamOrganization.setHigherUser(recommend_man);
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
    @Override
    public List<OrderDTO> findByOrderTypeAndOrderStatusAndUser(OrderType orderType, OrderStatus status, String phone) {
        User user = userRepository.findByPhone(phone);
        List<Order> orderList = user.getServiceOrderList().stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toList());
        return orderMapper.orderToOrderDTOList(orderList);
    }
}

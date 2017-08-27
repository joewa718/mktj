package com.mktj.cn.web.service.imp;

import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mktj.cn.web.configuration.WxPayProperties;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.enumerate.*;
import com.mktj.cn.web.mapper.OrderMapper;
import com.mktj.cn.web.po.*;
import com.mktj.cn.web.repositories.*;
import com.mktj.cn.web.service.BaseService;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.util.DateUtil;
import com.mktj.cn.web.vo.OrderVo;
import com.mktj.cn.web.vo.PayCertificateVo;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class OrderServiceImp extends BaseService implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "wxPayService")
    private WxPayService wxPayService;
    @Autowired
    WxPayProperties wxPayProperties;
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

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO applyOrder(String phone, OrderVo orderVo) {
        User user = userRepository.findByPhone(phone);
        if (user.isWeUser() && !user.isVerificationPhone()) {
            throw new RuntimeException("您是微信用户还未验证过手机，请先设置手机");
        }
        Product product = productRepository.findOne(orderVo.getProductId());
        if (product == null) {
            throw new RuntimeException("无法找到对应的商品");
        }
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIdAndUser(orderVo.getDeliverAddressId(), user);
        if (deliveryAddress == null) {
            throw new RuntimeException("无法找到对应的收货地址");
        }
        if (orderVo.getPayType() == PayType.线下转账 && StringUtils.isBlank(orderVo.getRecommendPhone())) {
            throw new RuntimeException("线下订单推荐人不能为空");
        }
        if (orderVo.getRecommendPhone().equals(user.getPhone())) {
            throw new RuntimeException("推荐人不能是本人");
        }
        if (userRepository.findOffspringCountByOrgPathAndPhone(getLikeStr(user), orderVo.getRecommendPhone()) != null) {
            throw new RuntimeException("推荐人不能是自己的下属");
        }
        if (user.getHigher() != null && !user.getHigher().getPhone().equals(orderVo.getRecommendPhone())) {
            throw new RuntimeException("推荐人必须是自己的上级,您的上级是(" + user.getHigher().getPhone() + ")");
        }
        User recommend_man = userRepository.findByPhone(orderVo.getRecommendPhone());
        if (recommend_man == null) {
            throw new RuntimeException("推荐人没有找到");
        }
        if (recommend_man.getRoleType() == RoleType.普通) {
            throw new RuntimeException("该推荐人不是合伙人");
        }
        if (recommend_man.isWeUser() && !recommend_man.isVerificationPhone()) {
            throw new RuntimeException("你的推荐人还未验证过手机，无法填写");
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
        Order order = saveOrder(orderVo, user, product, deliveryAddress, piece, price, totalCost);
        order.getHigherUserList().add(recommend_man);
        recommend_man.getServiceOrderList().add(order);
        userRepository.save(recommend_man);
        return orderMapper.orderToOrderDTO(order);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO savePayCert(String phone, PayCertificateVo payCertificateVo) {
        Order order = orderRepository.findOne(payCertificateVo.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getOrderStatus() != OrderStatus.待支付 && order.getOrderStatus() != OrderStatus.待确认) {
            throw new RuntimeException("订单状态必须是待支付");
        }
        if (order.getPayWay() != PayType.线下转账) {
            throw new RuntimeException("上传凭证，必须是线下订单类型");
        }
        if (StringUtils.isBlank(order.getRecommendPhone())) {
            throw new RuntimeException("线下订单推荐人不能为空");
        }
        User recommend_man = userRepository.findByPhone(order.getRecommendPhone());
        if (recommend_man == null) {
            throw new RuntimeException("推荐人没有找到");
        }
        if (payCertificateVo.getPayCertPhoto() == null || payCertificateVo.getPayCertPhoto().length == 0) {
            throw new RuntimeException("凭证照片不能为空");
        }
        if (StringUtils.isBlank(payCertificateVo.getPayCertInfo())) {
            throw new RuntimeException("凭证信息不能为空");
        }
        String[] payCentPhoto = payCertificateVo.getPayCertPhoto();
        order.setPayCertPhoto(StringUtils.join(payCentPhoto, ","));
        order.setPayCertInfo(payCertificateVo.getPayCertInfo());
        order.setOrderStatus(OrderStatus.待确认);
        order = orderRepository.save(order);
        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
        return orderDTO;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public void payWsSuccess(String orderCode) {
        Order order = orderRepository.findOneByOrderCode(orderCode);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getPayWay() != PayType.余额支付) {
            throw new RuntimeException("支付订单,必须是余额支付类型");
        }
        User user = order.getUser();
        User recommendMan = userRepository.findByPhone(order.getRecommendPhone());
        orderRepository.updateOrderStatusByIdAndUser(OrderStatus.已支付, order.getId(), user);
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        setPayRoleType(user, product);
        if (user.getHigher() == null && user.getRoleType().getCode() > RoleType.普通.getCode()) {
            List<User> offspringUser = userRepository.findByLikeOrgPath(getLikeStr(user));
            if (offspringUser != null && offspringUser.size() > 0) {
                offspringUser.forEach(lower -> {
                    lower.setOrgPath(bindOffSpringOrgPath(recommendMan, lower));
                    userRepository.save(lower);
                });
            }
            recommendMan.getLower().add(user);
            user.setHigher(recommendMan);
            user.setOrgPath(bindOffSpringOrgPath(recommendMan, user));
        }
        userRepository.save(order.getUser());
    }

    @Override
    public Map payOrder(long orderId) {
        Order order = orderRepository.findOne(orderId);
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOpenid(order.getUser().getoAuthInfo().getOpenId());
            orderRequest.setBody(order.getProductName() + "订单支付");
            orderRequest.setOutTradeNo(order.getOrderCode());
            orderRequest.setAppid(wxPayProperties.getAppId());
            orderRequest.setMchId(wxPayProperties.getMchId());
            orderRequest.setTotalFee(WxPayBaseRequest.yuanToFee("0.01"));//元转成分
            orderRequest.setSpbillCreateIp("122.152.208.113");
            orderRequest.setTradeType("JSAPI");
            orderRequest.setNotifyURL("http://www.jinhuishengwu.cn/api/wechat/pay/payNotice");
            Map wxPayUnifiedOrderResult = wxPayService.getPayInfo(orderRequest);
            logger.debug(wxPayUnifiedOrderResult.toString());
            return wxPayUnifiedOrderResult;
        } catch (Exception e) {
            String error = "微信支付失败！订单号：{" + order.getOrderCode() + "},原因:{" + e.getMessage() + "}";
            logger.error(error);
            throw new RuntimeException(error);
        }
    }

    private void setPayRoleType(User user, Product product) {
        if (product.getProductType() == ProductType.套餐产品 && product.getRoleType().getCode() > user.getRoleType().getCode()) {
            if (user.getAuthorizationCode() == null) {
                user.setAuthorizationCode(generateAuthCode());
            }
            user.setRoleType(product.getRoleType());
        }
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public OrderDTO sureOrder(String phone, long orderId) {
        User user = userRepository.findByPhone(phone);
        Order order = orderRepository.findOne(orderId);
        //线下转账，由属确认支付
        if (StringUtils.isBlank(order.getRecommendPhone()) || !user.getPhone().equals(order.getRecommendPhone())) {
            throw new RuntimeException("对不起，线下转账为成功，需要推荐人确认支付");
        }
        if (order.getPayWay() != PayType.线下转账) {
            throw new RuntimeException("确认订单，必须是线下转账类型");
        }
        if (order.getOrderStatus() != OrderStatus.待确认) {
            throw new RuntimeException("订单状态必须是待确认");
        }
        if (order.getUser().getHigher() != null && !user.getPhone().equals(order.getUser().getHigher().getPhone())) {
            throw new RuntimeException("该用户已经有上级（" + order.getUser().getHigher().getPhone() + "），先取消订单重新提交");
        }
        orderRepository.updateOrderStatusByIdAndUser(OrderStatus.已支付, orderId, order.getUser());
        order.setOrderStatus(OrderStatus.已支付);
        Product product = productRepository.getProductByproductCode(order.getProductCode());
        setPayRoleType(order.getUser(), product);
        if (order.getUser().getHigher() == null && order.getUser().getRoleType().getCode() > RoleType.普通.getCode()) {
            List<User> offspringUser = userRepository.findByLikeOrgPath(getLikeStr(order.getUser()));
            if (offspringUser != null && offspringUser.size() > 0) {
                offspringUser.forEach(lower -> {
                    lower.setOrgPath(bindOffSpringOrgPath(user, lower));
                    userRepository.save(lower);
                });
            }
            user.getLower().add(order.getUser());
            order.getUser().setHigher(user);
            order.getUser().setOrgPath(bindOffSpringOrgPath(user, order.getUser()));
        }
        userRepository.save(order.getUser());
        return orderMapper.orderToOrderDTO(order);
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    private Order saveOrder(OrderVo orderVo, User user, Product product, DeliveryAddress deliveryAddress, int piece, BigDecimal price, BigDecimal totalCost) {
        Order order = new Order();
        order.setOrderCode(generateOrderCode(String.valueOf(user.getId())));
        order.setUser(user);
        order.setOrderStatus(OrderStatus.待支付);
        order.setOrderTime(DateUtil.getCurrentDate());
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
        order.setSendManHead(product.getSendMan());
        order.setSendPhone(product.getSendPhone());
        order.setRecommendPhone(orderVo.getRecommendPhone());
        Optional<Set<Order>> orderList = Optional.ofNullable(user.getOrderList());
        orderList.orElse(new TreeSet<>());
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
        Set<Order> orderList;
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
        Set<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }
        if (status != OrderStatus.全部订单) {
            orderList = orderList.stream().filter(order -> order.getOrderStatus() == status).collect(Collectors.toSet());
        }
        List<OrderDTO> orderDTOList = orderMapper.orderToOrderDTOList(orderList);
        orderDTOList.forEach(orderDTO -> orderDTO.setOrderType(orderType.getName()));
        return orderDTOList;
    }

    @Override
    public Map<String, Long> summaryOrderCount(String phone, OrderType orderType) {
        User user = userRepository.findByPhone(phone);
        Set<Order> orderList;
        if (orderType == OrderType.进货订单) {
            orderList = user.getOrderList();
        } else {
            orderList = user.getServiceOrderList();
        }

        Map<Integer, Long> groupResult = orderList.stream().collect(Collectors.groupingBy(order -> order.getOrderStatus().getCode(), Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        long unPay = groupResult.get(OrderStatus.待支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.待支付.getCode());
        long alConfirm = groupResult.get(OrderStatus.待确认.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.待确认.getCode());
        long alPay = groupResult.get(OrderStatus.已支付.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已支付.getCode());
        long alSend = groupResult.get(OrderStatus.已发货.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已发货.getCode());
        long complete = groupResult.get(OrderStatus.已完成.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已完成.getCode());
        long cancel = groupResult.get(OrderStatus.已取消.getCode()) == null ? Long.valueOf(0) : groupResult.get(OrderStatus.已取消.getCode());
        map.put("全部订单", Long.valueOf(unPay + alConfirm + alPay + alSend + complete + cancel));
        map.put(OrderStatus.待支付.getName(), unPay);
        map.put(OrderStatus.待确认.getName(), alConfirm);
        map.put(OrderStatus.已支付.getName(), alPay);
        map.put(OrderStatus.已发货.getName(), alSend);
        map.put(OrderStatus.已完成.getName(), complete);
        map.put(OrderStatus.已取消.getName(), cancel);

        return map;
    }
}

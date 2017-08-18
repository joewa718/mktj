package com.mktj.cn.web.po;

import com.mktj.cn.web.converter.OrderStatusConverter;
import com.mktj.cn.web.converter.OrderTypeConverter;
import com.mktj.cn.web.converter.PayTypeConverter;
import com.mktj.cn.web.enu.OrderStatus;
import com.mktj.cn.web.enu.PayType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-08-11 17:19
 **/
@Entity
@Table(name = "t_order")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "order_code")
    private String orderCode;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private String receiverPhone;
    @Column(name = "receiver_province")
    private String receiverProvince;
    @Column(name = "receiver_city")
    private String receiverCity;
    @Column(name = "receiver_region")
    private String receiverRegion;
    @Column(name = "receiver_detailed")
    private String receiverDetailed;
    @Column(name = "send_name")
    private String sendName;
    @Column(name = "send_phone")
    private String sendPhone;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_price")
    private BigDecimal productPrice;
    @Column(name = "product_cost")
    private BigDecimal productCost;
    @Column(name = "product_num")
    private int productNum;
    @Convert(converter = PayTypeConverter.class)
    @Column(name = "pay_way")
    private PayType payWay;
    @Column(name = "order_comment")
    private String orderComment;
    @Convert(converter = OrderTypeConverter.class)
    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;
    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "recommend_phone")
    private String recommendPhone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "serviceOrderList",fetch = FetchType.LAZY)
    private List<User> higherUserList = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public PayType getPayWay() {
        return payWay;
    }

    public void setPayWay(PayType payWay) {
        this.payWay = payWay;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductCost() {
        return productCost;
    }

    public void setProductCost(BigDecimal productCost) {
        this.productCost = productCost;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailed() {
        return receiverDetailed;
    }

    public void setReceiverDetailed(String receiverDetailed) {
        this.receiverDetailed = receiverDetailed;
    }

    public String getRecommendPhone() {
        return recommendPhone;
    }

    public void setRecommendPhone(String recommendPhone) {
        this.recommendPhone = recommendPhone;
    }

    public List<User> getHigherUserList() {
        return higherUserList;
    }

    public void setHigherUserList(List<User> higherUserList) {
        this.higherUserList = higherUserList;
    }

}

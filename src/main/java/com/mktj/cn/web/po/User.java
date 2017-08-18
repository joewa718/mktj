package com.mktj.cn.web.po;

import com.mktj.cn.web.converter.RoleTypeConverter;
import com.mktj.cn.web.enu.RoleType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_user")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    private long id;
    @Field
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "head_Portrait")
    private String headPortrait;
    @Field
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "password", nullable = false)
    private String password;
    @Field
    @Column(name = "email")
    private String email;
    @Column(name = "score")
    private BigDecimal score = BigDecimal.valueOf(0);
    @Column(name = "disable")
    private Boolean disable = false;
    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "role_type")
    private RoleType roleType;
    @Column(name = "authorization_code")
    private String authorizationCode;
    @Column(name = "isReceiveMessage")
    private Boolean isReceiveMessage = false;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private RealInfo realInfo;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliveryAddress> deliveryAddressList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orderList;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderAnalysis orderAnalysis;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ServiceOrderAnalysis serviceOrderAnalysis;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TeamAnalysis teamAnalysis;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "lowerUser", fetch = FetchType.LAZY)
    private List<TeamOrganization> lowerList =new ArrayList<>();
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "higherUser", fetch = FetchType.LAZY)
    private List<TeamOrganization> higherUserList = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "t_user_service_order",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_id")}
    )
    private List<Order> serviceOrderList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Boolean getReceiveMessage() {
        return isReceiveMessage;
    }

    public void setReceiveMessage(Boolean receiveMessage) {
        isReceiveMessage = receiveMessage;
    }

    public RealInfo getRealInfo() {
        return realInfo;
    }

    public void setRealInfo(RealInfo realInfo) {
        this.realInfo = realInfo;
    }

    public List<DeliveryAddress> getDeliveryAddressList() {
        return deliveryAddressList;
    }

    public void setDeliveryAddressList(List<DeliveryAddress> deliveryAddressList) {
        this.deliveryAddressList = deliveryAddressList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }


    public OrderAnalysis getOrderAnalysis() {
        return orderAnalysis;
    }

    public void setOrderAnalysis(OrderAnalysis orderAnalysis) {
        this.orderAnalysis = orderAnalysis;
    }

    public ServiceOrderAnalysis getServiceOrderAnalysis() {
        return serviceOrderAnalysis;
    }

    public void setServiceOrderAnalysis(ServiceOrderAnalysis serviceOrderAnalysis) {
        this.serviceOrderAnalysis = serviceOrderAnalysis;
    }

    public TeamAnalysis getTeamAnalysis() {
        return teamAnalysis;
    }

    public void setTeamAnalysis(TeamAnalysis teamAnalysis) {
        this.teamAnalysis = teamAnalysis;
    }

    public List<TeamOrganization> getLowerList() {
        return lowerList;
    }

    public void setLowerList(List<TeamOrganization> lowerList) {
        this.lowerList = lowerList;
    }


    public List<Order> getServiceOrderList() {
        return serviceOrderList;
    }

    public void setServiceOrderList(List<Order> serviceOrderList) {
        this.serviceOrderList = serviceOrderList;
    }


    public List<TeamOrganization> getHigherUserList() {
        return higherUserList;
    }

    public void setHigherUserList(List<TeamOrganization> higherUserList) {
        this.higherUserList = higherUserList;
    }

}

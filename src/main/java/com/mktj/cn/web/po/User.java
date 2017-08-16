package com.mktj.cn.web.po;

import com.mktj.cn.web.converter.RoleTypeConverter;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.util.RoleType;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orderList;
    @NotFound ( action = NotFoundAction.IGNORE )
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderAnalysis> orderAnalysesList;
    @NotFound ( action = NotFoundAction.IGNORE )
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TeamAnalysis teamAnalysis;
    @NotFound ( action = NotFoundAction.IGNORE )
    @OneToMany(mappedBy = "lowerUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeamOrganization> lowerList;
    @NotFound ( action = NotFoundAction.IGNORE )
    @OneToMany(mappedBy = "higherUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeamOrganization> higherUser;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="t_user_service_order",
            joinColumns={@JoinColumn(name="user_id")},
            inverseJoinColumns={@JoinColumn(name="order_id")}
    )
    private List<Order> serviceOrder;

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

    public List<OrderAnalysis> getOrderAnalysesList() {
        return orderAnalysesList;
    }

    public void setOrderAnalysesList(List<OrderAnalysis> orderAnalysesList) {
        this.orderAnalysesList = orderAnalysesList;
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

    public List<TeamOrganization> getHigherUser() {
        return higherUser;
    }

    public void setHigherUser(List<TeamOrganization> higherUser) {
        this.higherUser = higherUser;
    }

    public List<Order> getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(List<Order> serviceOrder) {
        this.serviceOrder = serviceOrder;
    }
}

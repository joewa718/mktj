package com.mktj.cn.web.dto;

import java.math.BigDecimal;

/**
 * @author zhanwang
 * @create 2017-08-09 13:26
 **/
public class ProductDTO {

    private long id;
    private String product_code;
    private String productName;
    private String productImage;
    private String description;
    private BigDecimal retailPrice;
    private Integer piece;
    private String releaseTime;
    private Boolean isOffShelf;
    private String roleType;

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Boolean getOffShelf() {
        return isOffShelf;
    }

    public void setOffShelf(Boolean offShelf) {
        isOffShelf = offShelf;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }
}

package com.mktj.cn.web.dto;

/**
 * Created by waxi7002 on 7/3/2017.
 */
public class RankingDto {
    private String brandHashCode;

    private String brandDescEn;

    private String brandDescZh;

    private Double sumSalesValue;

    private String saleValueGrowth;

    private String saleValueShareChange;

    private Double savleValueShare;

    public String getBrandHashCode() {
        return brandHashCode;
    }

    public void setBrandHashCode(String brandHashCode) {
        this.brandHashCode = brandHashCode;
    }

    public String getBrandDescEn() {
        return brandDescEn;
    }

    public void setBrandDescEn(String brandDescEn) {
        this.brandDescEn = brandDescEn;
    }

    public String getBrandDescZh() {
        return brandDescZh;
    }

    public void setBrandDescZh(String brandDescZh) {
        this.brandDescZh = brandDescZh;
    }

    public Double getSumSalesValue() {
        return sumSalesValue;
    }

    public void setSumSalesValue(Double sumSalesValue) {
        this.sumSalesValue = sumSalesValue;
    }

    public String getSaleValueGrowth() {
        return saleValueGrowth;
    }

    public void setSaleValueGrowth(String saleValueGrowth) {
        this.saleValueGrowth = saleValueGrowth;
    }

    public String getSaleValueShareChange() {
        return saleValueShareChange;
    }

    public void setSaleValueShareChange(String saleValueShareChange) {
        this.saleValueShareChange = saleValueShareChange;
    }

    public Double getSavleValueShare() {
        return savleValueShare;
    }

    public void setSavleValueShare(Double savleValueShare) {
        this.savleValueShare = savleValueShare;
    }
}

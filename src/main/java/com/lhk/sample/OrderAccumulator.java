package com.lhk.sample;

import java.util.HashSet;

/**
 * @description: 聚合类
 * @author: huikang.lv
 * @create: 2020-02-11 23:11
 */
public class OrderAccumulator {
    private Long siteId;
    private String siteName;

    private HashSet<Long> orderIds;

    private Long subOrderSum;

    private Long quantitySum;

    private Long gmv;


    public void addOrderIds(HashSet<Long> orderIds) {
        this.orderIds.addAll(orderIds);
    }

    public void addOrderId(Long orderId) {
        this.orderIds.add(orderId);
    }

    public void addSubOrderSum(Long subOrderSum) {
        this.subOrderSum += subOrderSum;
    }

    public void addQuantitySum(Long quantitySum) {
        this.quantitySum += quantitySum;
    }

    public void addGMV(Long gmv) {
        this.gmv += gmv;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public HashSet<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(HashSet<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public Long getSubOrderSum() {
        return subOrderSum;
    }

    public void setSubOrderSum(Long subOrderSum) {
        this.subOrderSum = subOrderSum;
    }

    public Long getQuantitySum() {
        return quantitySum;
    }

    public void setQuantitySum(Long quantitySum) {
        this.quantitySum = quantitySum;
    }

    public Long getGmv() {
        return gmv;
    }

    public void setGmv(Long gmv) {
        this.gmv = gmv;
    }
}

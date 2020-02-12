package com.lhk.sample;

import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-11 23:09
 */
public class OrderAndGMVAggregateFunc implements AggregateFunction<SubOrderDetail, OrderAccumulator, OrderAccumulator> {
    @Override
    public OrderAccumulator createAccumulator() {
        return new OrderAccumulator();
    }

    @Override
    public OrderAccumulator add(SubOrderDetail subOrderDetail, OrderAccumulator orderAccumulator) {
        if (orderAccumulator.getSiteId() == null) {
            orderAccumulator.setSiteId(subOrderDetail.getSiteId());
            orderAccumulator.setSiteName(subOrderDetail.getSiteName());
        }
        orderAccumulator.addOrderId(subOrderDetail.getOrderId());
        orderAccumulator.addQuantitySum(subOrderDetail.getQuantity());
        orderAccumulator.addSubOrderSum(1L);
        orderAccumulator.addGMV(subOrderDetail.getPrice() * subOrderDetail.getQuantity());

        return orderAccumulator;
    }

    @Override
    public OrderAccumulator getResult(OrderAccumulator orderAccumulator) {
        return orderAccumulator;
    }

    @Override
    public OrderAccumulator merge(OrderAccumulator orderAccumulator, OrderAccumulator acc1) {
        if (orderAccumulator.getSiteId() == null) {
            orderAccumulator.setSiteId(acc1.getSiteId());
            orderAccumulator.setSiteName(acc1.getSiteName());
        }
        orderAccumulator.addOrderIds(acc1.getOrderIds());
        orderAccumulator.addQuantitySum(acc1.getQuantitySum());
        orderAccumulator.addSubOrderSum(acc1.getSubOrderSum());
        orderAccumulator.addGMV(acc1.getGmv());

        return orderAccumulator;
    }
}

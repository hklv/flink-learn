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
        return null;
    }

    @Override
    public OrderAccumulator add(SubOrderDetail subOrderDetail, OrderAccumulator orderAccumulator) {
        return null;
    }

    @Override
    public OrderAccumulator getResult(OrderAccumulator orderAccumulator) {
        return null;
    }

    @Override
    public OrderAccumulator merge(OrderAccumulator orderAccumulator, OrderAccumulator acc1) {
        return null;
    }
}

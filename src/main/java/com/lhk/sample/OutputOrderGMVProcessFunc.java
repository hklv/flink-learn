package com.lhk.sample;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-11 23:26
 */
public class OutputOrderGMVProcessFunc extends KeyedProcessFunction<Tuple, OrderAccumulator, Tuple2<Long, String>> {
    @Override
    public void processElement(OrderAccumulator orderAccumulator, Context context, Collector<Tuple2<Long, String>> collector) throws Exception {

    }
}

package com.lhk.apitest;

import com.lhk.source.SensorReading;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-23 22:52
 */
public class MyFilterFunction implements org.apache.flink.api.common.functions.FilterFunction<com.lhk.source.SensorReading> {
    @Override
    public boolean filter(SensorReading sensorReading) throws Exception {
        return sensorReading.getId().startsWith("sensor_1");
    }
}

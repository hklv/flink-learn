package com.lhk.source;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-09 23:33
 */
public class SensorReading {
    String id;
    long timestamp;
    Double temperature;

    public SensorReading(String id, long timestamp, Double temperature) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}

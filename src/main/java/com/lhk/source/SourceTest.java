package com.lhk.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Arrays;
import java.util.Properties;

/**
 * @description: source
 * @author: huikang.lv
 * @create: 2020-02-09 23:33
 */
public class SourceTest {
    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<SensorReading> stream1 = env.fromCollection(Arrays.asList(new SensorReading("sensor_1", 1547718199, 35.80018327300259),
                new SensorReading("sensor_6", 1547718201, 15.402984393403084),
                new SensorReading("sensor_7", 1547718202, 6.720945201171228),
                new SensorReading("sensor_10", 1547718205, 38.101067604893444)));

        DataStreamSource<String> stream2 = env.readTextFile("");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");
        DataStreamSource<String> stream3 = env.addSource(new FlinkKafkaConsumer011<String>("topic", new SimpleStringSchema(), properties));

        DataStreamSource<String> stream4 = env.addSource(new SensorSourceFunction());
    }
}

package com.lhk.sink;

import com.lhk.source.SensorReading;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

/**
 * @description: kafkaSink
 * @author: huikang.lv
 * @create: 2020-02-24 22:21
 */
public class KafkaSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> inputStream = env.readTextFile("D:\\project\\flinklearn\\src\\main\\resources\\sensor.txt");

        DataStream<String> dataStream = inputStream.map(data -> {
            String[] dataArray = data.split(",");
            return new SensorReading(dataArray[0], Long.valueOf(dataArray[1].trim()), Double.valueOf(dataArray[2].trim())).toString();
        });
        dataStream.addSink(new FlinkKafkaProducer011<>("localhost:9092", "sinkTest", new SimpleStringSchema()));
        dataStream.print();
        env.execute("kafka sink test");
    }
}

package com.lhk.sample;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

import java.util.Properties;

/**
 * @description: 订单的TopN
 * @author: huikang.lv
 * @create: 2020-02-11 22:45
 */
public class OrderTopN {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        env.enableCheckpointing(6 * 1000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(30 * 1000);
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers", "localhost:9092");
        consumerProperties.setProperty("group.id", "consumer-group");
        consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.setProperty("auto.offset.reset", "latest");

        DataStreamSource<String> inputSream = env.addSource(new FlinkKafkaConsumer011<String>("topic", new SimpleStringSchema(), consumerProperties));
        inputSream.setParallelism(2);
        inputSream.name("source_kafka_" + "topic");
        inputSream.uid("source_kafka_" + "topic");

        DataStream<SubOrderDetail> orderStream = inputSream.map(message -> JSON.parseObject(message, SubOrderDetail.class))
                .name("map_sub_order_detail")
                .uid("map_sub_order_detail");

        WindowedStream<SubOrderDetail, Tuple, TimeWindow> siteDayWindow = orderStream.keyBy("siteId")
                .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(1)));

        DataStream<OrderAccumulator> siteAggStream = siteDayWindow.aggregate(new OrderAndGMVAggregateFunc())
                .name("aggregate_site_order_gmv").uid("aggregate_site_order_gmv");

        DataStream<Tuple2<Long, String>> siteResultStream = siteAggStream.keyBy(0)
                .process(new OutputOrderGMVProcessFunc(), TypeInformation.of(new TypeHint<Tuple2<Long, String>>() {
                }))
                .name("process_site_gmv_changed")
                .uid("process_site_gmv_changed");

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder()
                .setHost("localhost")
                .setPort(6379)
                .build();

        siteResultStream.addSink(new RedisSink<>(conf, new GMVRedisMapper()))
                .name("sink_redis_site_gmv").uid("sink_redis_site_gmv")
                .setParallelism(1);
       /* DataStream<SubOrderDetail> orderStream = sourceStream
                .map(message -> JSON.parseObject(message, SubOrderDetail.class))
                .name("map_sub_order_detail").uid("map_sub_order_detail");*/
    }

}

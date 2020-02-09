package com.lhk.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @description: 从文件数据源统计
 * @author: huikang.lv
 * @create: 2020-02-09 17:34
 */
public class WordCountFromTuple {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.setStateBackend(new MemoryStateBackend());

        DataStreamSource<String> dataStream = env.socketTextStream("localhost", 8080);

        dataStream.flatMap(new FlatMapFunction<String, Tuple>() {
            @Override
            public void flatMap(String s, Collector<Tuple> collector) throws Exception {
                for (String word : s.split("\\s")) {
                    collector.collect(new Tuple2(word, 1L));
                }
            }
        }).keyBy(0).sum(1).print();

        env.execute("word count");
    }
}

package com.lhk.watermark;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Main {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        SingleOutputStreamOperator<Word> data = env.socketTextStream("localhost", 8888)
                .map(new MapFunction<String, Word>() {
                    @Override
                    public Word map(String s) throws Exception {
                        String[] split = s.split(",");
                        return new Word(split[0], Integer.valueOf(split[1]), Long.valueOf(split[2]));
                    }
                });
        data.assignTimestampsAndWatermarks(new WordPunctuateWatermark());
        data.print();
        env.execute("watermark demo");
    }
}

package com.lhk.wordcount.state;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-29 22:22
 */
public class FlinkStatefulCalcTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> words = env.socketTextStream("localhost", 9999, "\n");

        words.flatMap(new WordFlatMapFunction())
               // .returns(WordCount.class)
                .keyBy("word")
                .flatMap(new WordCountFunction())
                .print();

        env.execute("stateful window WordCount");

    }
}

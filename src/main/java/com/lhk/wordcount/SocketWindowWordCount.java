package com.lhk.wordcount; /**
 * ymm56.com Inc.
 * Copyright (c) 2013-2019 All Rights Reserved.
 */

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author LvHuiKang
 * @version $Id: com.lhk.wordcount.SocketWindowWordCount.java, v 0.1 2019-03-11 16:59 LvHuiKang Exp $$
 */
public class SocketWindowWordCount {
   public static void main(String[] args) throws Exception {
        final String hostName;
        final int port;
        final ParameterTool params = ParameterTool.fromArgs(args);
        hostName = "127.0.0.1";
        port = 8080;
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> text = env.socketTextStream(hostName, port, "\n");
        DataStream<WordWithCount> windowCounts = text.flatMap(new FlatMapFunction<String, WordWithCount>() {
            @Override
            public void flatMap(String s, Collector<WordWithCount> collector) throws Exception {
                for (String word : s.split("\\s")) {
                    collector.collect(new WordWithCount(word, 1L));
                }
            }
        })
                .keyBy("word")
                .timeWindow(Time.seconds(5))
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, b.count + b.count);
                    }
                });
        windowCounts.print().setParallelism(1);
        env.execute("Socket Window WordCount");

    }

    public static class WordWithCount {
        public String word;
        public long count;

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }

        public WordWithCount() {
        }
    }
}
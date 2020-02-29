package com.lhk.wordcount.state;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-29 22:30
 */
public class WordFlatMapFunction implements FlatMapFunction<String, WordCount> {
    @Override
    public void flatMap(String s, Collector<WordCount> collector) throws Exception {
        for (String t : s.split(" ")) {
            collector.collect(new WordCount(t, 1));
        }
    }
}

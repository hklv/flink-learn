package com.lhk.wordcount.state;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-29 22:14
 */
public class WordCountFunction extends RichFlatMapFunction<WordCount, WordCount> {
    private transient ValueState<WordCountState> countState;

    @Override
    public void flatMap(WordCount wordCount, Collector<WordCount> out) throws Exception {
        WordCountState lastState = countState.value();
        if (lastState == null) {
            lastState = new WordCountState(wordCount.getWord(), wordCount.getCount());
            out.collect(wordCount);
            countState.update(lastState);
        } else {
            lastState.setCount(lastState.getCount() + wordCount.getCount());
            out.collect(new WordCount(wordCount.getWord(), lastState.getCount()));
            countState.update(lastState);
        }
    }

    @Override
    public void open(Configuration configuration) {
        ValueStateDescriptor<WordCountState> descriptor = new ValueStateDescriptor<WordCountState>("countState", TypeInformation.of(new TypeHint<WordCountState>() {
        }));
        countState = getRuntimeContext().getState(descriptor);
    }
}

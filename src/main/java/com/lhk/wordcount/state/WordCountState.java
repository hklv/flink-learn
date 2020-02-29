package com.lhk.wordcount.state;

import java.io.Serializable;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-29 22:12
 */
public class WordCountState implements Serializable {
    private String word;

    private Integer count;


    public WordCountState() {
    }


    public WordCountState(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

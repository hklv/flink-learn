package com.lhk.wordcount.state;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-29 22:11
 */
public class WordCount {
    private String word;
    private Integer count;

    public WordCount() {
    }

    public WordCount(String word, Integer count) {
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

    @Override
    public String toString() {
        return "WordCount{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }
}

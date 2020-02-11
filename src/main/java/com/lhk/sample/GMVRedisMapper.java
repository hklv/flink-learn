package com.lhk.sample;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-11 23:32
 */
public class GMVRedisMapper implements RedisMapper<Tuple2<Long, String>> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return null;
    }

    @Override
    public String getKeyFromData(Tuple2<Long, String> longStringTuple2) {
        return null;
    }

    @Override
    public String getValueFromData(Tuple2<Long, String> longStringTuple2) {
        return null;
    }
}

package com.lhk.sample;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-11 23:32
 */
public class GMVRedisMapper implements RedisMapper<Tuple2<Long, String>> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.HSET, "RT:DASHBOARD:GMV:");
    }

    @Override
    public String getKeyFromData(Tuple2<Long, String> longStringTuple2) {
        return String.valueOf(longStringTuple2.f0);
    }

    @Override
    public String getValueFromData(Tuple2<Long, String> longStringTuple2) {
        return longStringTuple2.f1;
    }

}

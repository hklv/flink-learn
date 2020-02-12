package com.lhk.sample;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-11 23:26
 */
public class OutputOrderGMVProcessFunc extends KeyedProcessFunction<Tuple, OrderAccumulator, Tuple2<Long, String>> {
    private MapState<Long, OrderAccumulator> mapState;

    @Override
    public void open(Configuration configuration) throws Exception {
        super.open(configuration);
        mapState = this.getRuntimeContext().getMapState(new MapStateDescriptor<Long, OrderAccumulator>("state_site_order_gmv", Long.class, OrderAccumulator.class));

    }

    @Override
    public void processElement(OrderAccumulator orderAccumulator, Context context, Collector<Tuple2<Long, String>> collector) throws Exception {
        long key = orderAccumulator.getSiteId();
        OrderAccumulator cacheOrderAccumulator = mapState.get(key);
        if (cacheOrderAccumulator != null || orderAccumulator.getSubOrderSum() != cacheOrderAccumulator.getSubOrderSum()) {
            JSONObject result = new JSONObject();
            result.put("site_id", orderAccumulator.getSiteId());
            result.put("site_name", orderAccumulator.getSiteName());
            result.put("quantity", orderAccumulator.getQuantitySum());
            result.put("orderCount", orderAccumulator.getOrderIds().size());
            result.put("subOrderCount", orderAccumulator.getSubOrderSum());
            result.put("gmv", orderAccumulator.getGmv());
            collector.collect(new Tuple2<>(key, result.toJSONString()));
            mapState.put(key, orderAccumulator);
        }
    }

    @Override
    public void close() throws Exception {
        mapState.clear();
        super.close();
    }
}

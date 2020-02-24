package com.lhk.apitest;

import com.lhk.source.SensorReading;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huikang.lv
 * @create: 2020-02-23 21:40
 */
public class TransformTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> inputStream = env.readTextFile("D:\\project\\flinklearn\\src\\main\\resources\\sensor.txt");


        DataStream<SensorReading> dataStream = inputStream.map(data -> {
            String[] dataArray = data.split(",");
            return new SensorReading(dataArray[0], Long.valueOf(dataArray[1].trim()), Double.valueOf(dataArray[2].trim()));
        });

        DataStream<SensorReading> stream1 = dataStream.keyBy("id").reduce((x, y0) -> new SensorReading(x.getId(), x.getTimestamp() + 1, y0.getTemperature() + 10));

        DataStream<SensorReading> splitStream = dataStream.split(sensorData -> {
            List<String> hightList = new ArrayList<>();
            List<String> lowtList = new ArrayList<>();
            if (sensorData.getTemperature() > 30) {
                hightList.add("high");
                return hightList;
            } else {
                lowtList.add("low");
                return lowtList;
            }
        });


        DataStream<SensorReading> highTempStream = ((SplitStream<SensorReading>) splitStream).select("high");
        DataStream<SensorReading> lowTempStream = ((SplitStream<SensorReading>) splitStream).select("low");
        DataStream<SensorReading> allTempStream = ((SplitStream<SensorReading>) splitStream).select("high", "low");

        DataStream<TestClass> warningStream = highTempStream.map(sensorData -> new TestClass(sensorData.getId(), sensorData.getTemperature()));
        ConnectedStreams<TestClass, SensorReading> connectStream = warningStream.connect(lowTempStream);

        SingleOutputStreamOperator<Object> coMapStream = connectStream.map(new CoMapFunction<TestClass, SensorReading, Object>() {
            @Override
            public Object map1(TestClass testClass) throws Exception {
                return new TestOutputClass(testClass.id, testClass.temperature, "high temperature warning");
            }

            @Override
            public Object map2(SensorReading sensorReading) throws Exception {
                return new TestOutputClass(sensorReading.getId(), "healthy");
            }
        });

        DataStream<SensorReading> unionStream = highTempStream.union(lowTempStream);

        dataStream.filter(new MyFilterFunction()).print();
        // stream1.print("reduce");
        //highTempStream.print("high");
        //lowTempStream.print("low");
        //allTempStream.print("all");
        //coMapStream.print();
        //unionStream.print("union");
        env.execute();


    }

    public static class TestOutputClass {
        String id;
        Double temperature;
        String name;

        public TestOutputClass(String id, Double temperature, String name) {
            this.id = id;
            this.temperature = temperature;
            this.name = name;
        }

        public TestOutputClass(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public TestOutputClass() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestOutputClass{" +
                    "id='" + id + '\'' +
                    ", temperature=" + temperature +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class TestClass {

        String id;
        Double temperature;

        public TestClass() {
        }

        public TestClass(String id, Double temperature) {
            this.id = id;
            this.temperature = temperature;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
    }


    class MyMapper extends RichMapFunction<SensorReading,String> {
        @Override
        public String map(SensorReading sensorReading) throws Exception {
            return null;
        }
    }

    class MyFlatMapFunction extends RichFlatMapFunction<SensorReading,String>{
        @Override
        public void flatMap(SensorReading sensorReading, Collector<String> collector) throws Exception {

        }
    }

}

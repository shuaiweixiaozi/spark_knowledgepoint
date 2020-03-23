package myown.momo.kafka.example.myown.momo.kafkajiaocheng.example;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hzhb on 2017/9/14.
 */

/*
 This example is from kafka jiaocheng whose website is http://orchome.com/10.
 */
public class ConsumerGroupExample {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public ConsumerGroupExample(String aZookeeper,String aGroupId,String aTopic){
        this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(aZookeeper,aGroupId));
        this.topic = aTopic;
    }

    private static ConsumerConfig createConsumerConfig(String aZookeeper,String aGroupId){
        Properties props = new Properties();
        props.put("zookeeper.connect",aZookeeper);
        props.put("group.id",aGroupId);
        props.put("zookeeper.session.timeout.ms","400");
        props.put("zookeepre.sync.time.ms","200");
        props.put("auto.commit.interval.ms","1000");
        return new ConsumerConfig(props);
    }

    public void shutdown(){
        if(consumer!=null) consumer.shutdown();
        if(executor!=null) executor.shutdown();
    }

    public void run(int aNumThreads){
        Map<String,Integer> topicCountMap = new HashMap<String,Integer>();
        topicCountMap.put(topic,new Integer(aNumThreads));
        Map<String, List<KafkaStream<byte[],byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[],byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        executor = Executors.newFixedThreadPool(aNumThreads);

        //now create an object to consume the messages
        int threadNumber = 0;
        for(final KafkaStream stream:streams){
            executor.submit(new ConsumerTest(stream,threadNumber));
            threadNumber++;
        }
    }

    public static void main(String[] args){
        String zookeeper = "n1:2181,d1:2181,d2:2181";
        String groupId = "ConsumerGroupExample";
        String topic = "user_events";
        int threads = 4;

        ConsumerGroupExample example = new ConsumerGroupExample(zookeeper,groupId,topic);
        example.run(threads);

        try{
            Thread.sleep(10000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        example.shutdown();
    }
}

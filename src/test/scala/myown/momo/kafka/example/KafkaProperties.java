package myown.momo.kafka.example;

/**
 * Created by hzhb on 2017/9/9.
 */
public class KafkaProperties {
    public static final String TOPIC = "user_events";
    public static final String KAFKA_SERVER_URL = "n1:9092,d1:9092,d2:9092";
    public static final String KAFKA_SERVER_URL_Single = "192.168.1.160";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String TOPIC2 = "topic2";
    public static final String TOPIC3 = "topic3";
    public static final String CLIENT_ID = "SimpleConsumerDemoClient";

    private KafkaProperties(){}
}
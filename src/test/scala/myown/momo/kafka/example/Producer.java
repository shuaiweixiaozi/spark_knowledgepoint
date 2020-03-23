package myown.momo.kafka.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by hzhb on 2017/9/9.
 */
public class Producer extends Thread{
    private KafkaProducer<Integer,String> producer;
    private String topic;
    private Boolean isAsync;

    public Producer(String topic,Boolean isAsync){
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.KAFKA_SERVER_URL);
        props.put("client.id","DemoProducer");
//        props.put("serializer.class","kafka.serializer.DefaultEncoder");
        props.put("key.serializer","org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void run(){
        int messageNo = 1;
        while(true){
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if(isAsync){  //send asynchronously
                producer.send(new ProducerRecord<Integer, String>(topic,messageNo,messageStr),
                              new DemoCallBack(startTime,messageNo,messageStr));
            }else {    // send synchronously
                try {
                    producer.send(new ProducerRecord<Integer, String>(topic,messageNo,messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                }catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }
            ++messageNo;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class DemoCallBack implements Callback {
    private final long startTime;
    private final int key;
    private final String message;


    public DemoCallBack(long startTime,int key,String message){
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion.
     * This method will be called when the record sent to the server has been acknowledged. Exactly one
     * of the arguments will be non-null.
     * @param metadata The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(metadata != null){
            System.out.println("message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                                "), offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        }else {
            exception.printStackTrace();
        }
    }
}

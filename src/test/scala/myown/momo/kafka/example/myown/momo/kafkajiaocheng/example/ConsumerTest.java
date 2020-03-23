package myown.momo.kafka.example.myown.momo.kafkajiaocheng.example;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * Created by hzhb on 2017/9/14.
 */
public class ConsumerTest implements Runnable{
    private KafkaStream mStream;
    private int mThreadNumber;

    public ConsumerTest(KafkaStream aStream,int aThreadNumber){
        this.mStream = aStream;
        this.mThreadNumber = aThreadNumber;
    }

    public void run(){
        ConsumerIterator<byte[],byte[]> it = mStream.iterator();
        while(it.hasNext()){
            System.out.println("Thread " + mThreadNumber + ": " + new String(it.next().message()));
        }
        System.out.println("Shutting down Thread: " + mThreadNumber);
    }
}

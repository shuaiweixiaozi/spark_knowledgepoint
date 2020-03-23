package myown.momo.kafka.example.myown.momo.kafkajiaocheng.example;

//import kafka.api.*;
//import kafka.common.ErrorMapping;
//import kafka.common.OffsetAndMetadata;
//import kafka.common.OffsetMetadata;
//import kafka.common.TopicAndPartition;
//import kafka.javaapi.*;
//import kafka.javaapi.FetchResponse;
//import kafka.javaapi.OffsetCommitRequest;
//import kafka.javaapi.PartitionMetadata;
//import kafka.javaapi.TopicMetadata;
//import kafka.javaapi.TopicMetadataRequest;
//import kafka.javaapi.consumer.SimpleConsumer;
//import kafka.message.MessageAndOffset;
//
//import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by hzhb on 2017/9/14.
 */
public class SimpleExample {

    private List<String> mReplicationBrokers ;

    public static void main(String[] args){
//        SimpleExample example = new SimpleExample();
//        long maxReads = 10000L;
//        String topic = "user_events";
//        int partition = 1;
//        List<String> seeds = new ArrayList<String>();
//        seeds.add("192.168.1.160");
//        seeds.add("192.168.1.161");
//        seeds.add("192.168.1.162");
//        int port = 9092;
//        try{
//            example.run(maxReads,topic,partition,seeds,port);
//        }catch (Exception e){
//            System.out.println("Oops:" + e);
//            e.printStackTrace();
//        }
//    }
//
//    public SimpleExample(){
//        mReplicationBrokers = new ArrayList<String>();
//    }
//
//    public void run(long maxReads,String topic,int partition,List<String> seedBrokers,int port) throws Exception{
//        // find the meta data about the topic and partition we are interested in
//
//        PartitionMetadata metadata = findLeader(seedBrokers,port,topic,partition);
//        if(metadata == null){
//            System.out.println("Cann't find metadata for Topic and Partition.Exiting");
//            return;
//        }
//        if(metadata.leader() == null){
//            System.out.println("Cann't find Leader for topic and Partition.");
//            return;
//        }
//        String leadBroker = metadata.leader().host();
//        String clientName = "Client_" + topic + "_" + partition;
//
//        SimpleConsumer consumer = new SimpleConsumer(leadBroker,port,100000,64*1024,clientName);
//        long readOffset = getLastOffset(consumer,topic,partition,kafka.api.OffsetRequest.EarliestTime(),clientName);
//
//        int numErrors = 0;
//        while (maxReads > 0){
//            if(consumer == null){
//                consumer = new SimpleConsumer(leadBroker,port,100000,64*1024,clientName);
//            }
//            kafka.api.FetchRequest request = new FetchRequestBuilder()
//                    .clientId(clientName)
//                    .addFetch(topic,partition,readOffset,100000)   // Note:this fetchSize of 100000 might need to be increased if large batches are written to Kafka
//                    .build();
//            FetchResponse fetchResponse = consumer.fetch(request);
//
//            if(fetchResponse.hasError()){
//                numErrors++;
//                // Something went wrong
//                short code = fetchResponse.errorCode(topic,partition);
//                System.out.println("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
//                if (numErrors > 5) break;
//
//                if(code == ErrorMapping.OffsetOutOfRangeCode()){
//                    // We asked for an invalid offset. For simple case ask for the last element to reset
//                    readOffset = getLastOffset(consumer,topic,partition,kafka.api.OffsetRequest.LatestTime(),clientName);
//                    continue;
//                }
//                consumer.close();
//                consumer = null;
//                leadBroker = findNewLeader(leadBroker,topic,partition,port);
//                continue;
//            }
//            numErrors = 0;
//
//            long numRead = 0;
//            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic,partition)){
//                long currentOffset = messageAndOffset.offset();
//                if(currentOffset < readOffset){
//                    System.out.println("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
//                    continue;
//                }
//                readOffset = messageAndOffset.nextOffset();
//                ByteBuffer  payload = messageAndOffset.message().payload();
//
//                byte[] bytes = new byte[payload.limit()];
//                payload.get(bytes);
//                System.out.println(String.valueOf(messageAndOffset.offset()) + ": " + new String(bytes,"UTF-8"));
//
////                Map<TopicAndPartition,OffsetAndMetadata> requestInfoMap = new HashMap<TopicAndPartition,OffsetAndMetadata> ();
////                TopicAndPartition topicAndPartition = new TopicAndPartition(topic,partition);
////                requestInfoMap.put(topicAndPartition,new OffsetAndMetadata(new OffsetMetadata(readOffset,""),System.currentTimeMillis(),-1L));
////                kafka.javaapi.OffsetCommitRequest ocRequest = new OffsetCommitRequest(consumer.clientId(),requestInfoMap,0,consumer.clientId());
////                consumer.commitOffsets(ocRequest);
//
//                numRead++;
//                maxReads--;
//            }
//            if(numRead == 0){
//                try {
//                    Thread.sleep(1000);
//                }catch (InterruptedException e){ e.printStackTrace();}
//            }
//        }
//        if(consumer != null) consumer.close();
//    }
//
//    private String findNewLeader(String oldLeader,String topic,int partition,int port) throws Exception{
//        for(int i = 0;i < 3;i++){
//            boolean gotoSleep = false;
//            PartitionMetadata metadata = findLeader(mReplicationBrokers,port,topic,partition);
//            if(metadata == null) gotoSleep = true;
//            else if (metadata.leader() == null) gotoSleep = true;
//            else if (oldLeader.equalsIgnoreCase(metadata.leader().host()) && i==0){
//                // first time through if the leader hasn't changed give Zookeeper a second to recover
//                // second time,assume the broker did recover before failover,or it was a non-Broker issue
//                gotoSleep = true;
//            }
//            else {
//                return metadata.leader().host();
//            }
//            if (gotoSleep){
//                try {
//                    Thread.sleep(1000);
//                }catch (InterruptedException e) {e.printStackTrace();}
//            }
//        }
//        System.out.println("Unable to find new leader after Broker failure.Exiting");
//        throw new Exception("Unable to find new leader after Broker failure.Exiting");
//    }
//
//    public static long getLastOffset(SimpleConsumer consumer,String topic,int partition,long whichTime,String clientName){
//        TopicAndPartition topicAndPartition = new TopicAndPartition(topic,partition);
//        Map<TopicAndPartition,PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition,PartitionOffsetRequestInfo>();
//        requestInfo.put(topicAndPartition,new PartitionOffsetRequestInfo(whichTime,1));
//        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo,kafka.api.OffsetRequest.CurrentVersion(),clientName);
//        kafka.javaapi.OffsetResponse response = consumer.getOffsetsBefore(request);
//
//        if(response.hasError()){
//            System.out.println("Error fetching data Offset Data the Broker the Broker.Reason: " + response.errorCode(topic,partition));
//            return 0;
//        }
//        long[] offsets = response.offsets(topic,partition);
//        return offsets[0];
//    }
//
//    private PartitionMetadata findLeader(List<String> seedBrokers,int port,String topic,int partition){
//        PartitionMetadata returnMetaData = null;
//        loop:
//        for (String seed : seedBrokers){
//            SimpleConsumer consumer = null;
//            try{
//                consumer = new SimpleConsumer(seed,port,100000,64*1024,"leaderLookup");
//                List<String> topics = Collections.singletonList(topic);
//                TopicMetadataRequest req = new TopicMetadataRequest(topics);
//                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);
//
//                List<TopicMetadata> metaDatas = resp.topicsMetadata();
//                for(TopicMetadata item:metaDatas){
//                    for(PartitionMetadata part:item.partitionsMetadata()){
//                        if(part.partitionId() == partition){
//                            returnMetaData = part;
//                            break loop;
//                        }
//                    }
//                }
//            }catch (Exception e){
//                System.out.println("Error communicating with Broker [" + seed + "] to find Leader for [" + topic + ", " + partition + "] Reason:" + e);
//            }finally {
//                if(consumer != null) consumer.close();
//            }
//        }
//        if(returnMetaData != null){
//            mReplicationBrokers.clear();
//            for (kafka.cluster.BrokerEndPoint repication:returnMetaData.replicas()){
//                mReplicationBrokers.add(repication.host());
//            }
//        }
//        return returnMetaData;
    }
}
package com.hadoop.hbaseconsumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.InputStream;
import java.util.*;

public class HbaseConsumer {

    public HbaseConsumer() {
        hdao = new HbaseDao();
    }

    private HbaseDao hdao = null;

    public HbaseDao getHdao() {
        return hdao;
    }

    public void setHdao(HbaseDao hdao) {
        this.hdao = hdao;
    }

    public static void main(String[] args){
        HbaseConsumer hconsumer = new HbaseConsumer();
        hconsumer.consumer();
    }

    public void consumer(){
        try {
            InputStream in = HbaseConsumer.class.getClassLoader().getResourceAsStream("kafka.properties");
            Properties props = new Properties();
            props.load(in);

            ConsumerConfig conf = new ConsumerConfig(props);
            ConsumerConnector consumer = Consumer.createJavaConsumerConnector(conf);

            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("calllog",new Integer(1));

            Map<String,List<KafkaStream<byte[], byte[]>>> msg = consumer.createMessageStreams(map);
            List<KafkaStream<byte[], byte[]>> list = msg.get("calllog");
            for (KafkaStream<byte[], byte[]> stream : list ) {
                ConsumerIterator<byte[], byte[]> it = stream.iterator();
                while (it.hasNext()){
                    byte[] messsage = it.next().message();
                    System.out.println(new String(messsage));
                    hdao.put(new String(messsage));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

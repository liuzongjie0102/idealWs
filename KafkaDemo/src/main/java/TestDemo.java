import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestDemo {

    public static void main(String[] args){
       // sendDemo();
        testConsumer();
    }

    private static KafkaProducer<String, String> producer = null;
    private static ConsumerConnector consumer = null;

    static {
        //生产者配置文件，具体配置可参考ProducerConfig类源码，或者参考官网介绍
        Map<String, Object> config = new HashMap<String,Object>();
        //kafka服务器地址
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"s110:9092,s111:9092");
        //kafka消息序列化类 即将传入对象序列化为字节数组
        //config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        //kafka消息key序列化类 若传入key的值，则根据该key的值进行hash散列计算出在哪个partition上
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024*1024*5);
        //往kafka服务器提交消息间隔时间，0则立即提交不等待
        config.put(ProducerConfig.LINGER_MS_CONFIG,0);
        //消费者配置文件
        Properties props = new Properties();
        //zookeeper地址
        props.put("zookeeper.connect", "s110:2181");

        //组id
        props.put("group.id", "g1");
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        //自动提交消费情况间隔时间
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        producer = new KafkaProducer<String, String>(config);
        consumer = Consumer.createJavaConsumerConnector(consumerConfig);
    }

    public static void sendDemo(){
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test2","hello tomas");
        producer.send(record);
    }

    public static void testConsumer(){

        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("calllog",new Integer(1));

        Map<String, List<KafkaStream<byte[], byte[]>>> msgs =consumer.createMessageStreams(map);
        List<KafkaStream<byte[], byte[]>> list = msgs.get("calllog");

        for (KafkaStream<byte[], byte[]> stream : list){
            ConsumerIterator<byte[],byte[]> it = stream.iterator();
            while(it.hasNext()){
                byte[] message = it.next().message();
                System.out.println(new String(message));
            }
        }
    }

}

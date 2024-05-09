package zql.CallRope.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class kafkademo {
    private static final String KAFKA_TOPIC = "callrope-span";
    private static final Properties props = new Properties();
    private static final KafkaConsumer<String, String> consumer;
    private final static String CONSUMER_GROUP_NAME = "testGroup";
    private static final Properties properties = new Properties();
    private static final Producer<String, String> producer;
    static {
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "rope-span-consumer");
        // key的反序列化
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // value的反序列化
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 消费分组名
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.240.133:9092");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //把发送消息value从字符串序列化为字节数组
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.240.133:9092");
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh");
        producer = new KafkaProducer<>(properties);
    }
    public static void consumer() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received message: " + record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
    public static void sendSpanToKafka(String message) {

        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, message);
            System.out.println("%" + message);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    // todo 处理失败
                    System.err.println("发送消息失败：" + exception.getStackTrace());
                }
                if (null != metadata) {
                    System.out.println("异步方式发送消息结果：" + "topic-" + metadata.topic() + "|partition-"
                            + metadata.partition() + "|offset-" + metadata.offset());
                }
            });
        } finally {
            producer.flush();
        }
    }

    public static void main(String[] args) {
        consumer();
        sendSpanToKafka("hello world");
    }
}

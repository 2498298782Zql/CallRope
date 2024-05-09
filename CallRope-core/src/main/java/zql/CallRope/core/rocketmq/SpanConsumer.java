package zql.CallRope.core.rocketmq;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.kafka.common.message.StopReplicaResponseDataJsonConverter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.elasticsearch.client.transport.TransportClient;
import zql.CallRope.core.es.ESUtil;
import zql.CallRope.point.model.Span;

import java.util.List;

public class SpanConsumer {

    private static final DefaultMQPushConsumer consumer;
    private static final String CONSUMER_GROUP_NAME = "span-consumer";
    private static final String ROCKETMQ_TOPIC = "callrope-span";
    private static final String ES_INDEX = "rope";

    static {
        consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
        consumer.setNamesrvAddr("192.168.240.133:9876");
        try {
            consumer.subscribe(ROCKETMQ_TOPIC, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        ESUtil.getClient("elasticsearch","192.168.240.133", 9200);
        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    Span span = JSONObject.parseObject(msg.getBody(), Span.class);
                    ESUtil.addLogintoES("callRope", span, Span.class);
                    System.out.println("Received message: " + span);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }

    public static void start() {
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}


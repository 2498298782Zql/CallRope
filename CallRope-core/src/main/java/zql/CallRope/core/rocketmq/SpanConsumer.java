package zql.CallRope.core.rocketmq;

import co.elastic.clients.elasticsearch._types.Result;
import com.alibaba.fastjson.JSONObject;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import zql.CallRope.core.es.EsUtil;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.List;

public class SpanConsumer {
    private static final DefaultMQPushConsumer consumer;
    private static final String CONSUMER_GROUP_NAME = "span-consumer";
    private static final String ROCKETMQ_TOPIC = "callrope-span";
    private static final String ES_INDEX = "rope-span";
    private static String EsHostName = "192.168.240.133";
    private static String MQHostName = "192.168.240.133";
    private static Integer EsPort = 9200;
    private static String MQPort = "9876";

    static {
        consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
        try {
            consumer.setNamesrvAddr( MQHostName +":" + MQPort);
            consumer.subscribe(ROCKETMQ_TOPIC, "span");
            EsUtil.getClient(EsHostName, EsPort);
            EsUtil.initIndex(ES_INDEX);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        try {
                            Span span = JSONObject.parseObject(msg.getBody(), Span.class);
                            Result result = EsUtil.addDocument(ES_INDEX, span, span.traceId + "_" + span.spanId);
                        } catch (ObjectNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } catch (MQClientException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}


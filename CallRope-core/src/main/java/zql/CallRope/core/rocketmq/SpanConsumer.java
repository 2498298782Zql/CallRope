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
import zql.CallRope.core.config.Configuration;
import zql.CallRope.core.es.EsUtil;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.List;

import static zql.CallRope.core.config.Configuration.getProperty;
import static zql.CallRope.core.config.Configuration.getPropertyAsInteger;

public class SpanConsumer {
    private static final DefaultMQPushConsumer consumer;
    private static final String CONSUMER_GROUP_NAME;
    private static final String ROCKETMQ_TOPIC;
    private static final String MQHostName;
    private static final String MQPort;
    private static final String ES_INDEX;
    private static final String EsHostName;
    private static final Integer EsPort;

    static {
        EsHostName = getProperty("es_host_name");
        EsPort = getPropertyAsInteger("es_port");
        ES_INDEX = getProperty("es_index");
        CONSUMER_GROUP_NAME = getProperty("mq_comsumer_group");
        ROCKETMQ_TOPIC = getProperty("mq_topic");
        MQPort = getProperty("mq_port");
        MQHostName = getProperty("mq_host_name");

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


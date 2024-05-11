package zql.CallRope.core.rocketmq;


import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import zql.CallRope.point.model.Span;

import java.io.UnsupportedEncodingException;
import java.time.Year;

/** 引入mq的两个原因：
 * 业务handler里面的阻塞时间不能超过执行整个队列事件的时间,如果这个时候还有handler阻塞,disruptor就不能再写入数据了,所有生产者线程都会阻塞,而且因为大量线程LockSupport.parkNanos(1),还会消耗大量的性能
 * 消息的持久化存储
 */

public class SpanProducer {
    private static final String ROCKETMQ_TOPIC = "callrope-span";
    private static final DefaultMQProducer producer;
    private static final String MQ_IP_PORT = "192.168.240.133:9876";

    static {
        producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr(MQ_IP_PORT);
        try {
            producer.start();
            producer.setCreateTopicKey(ROCKETMQ_TOPIC);
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendSpanToRocketMq(Span span) {
        try {
            String message = JSONObject.toJSONString(span);
            Message msg = new Message(
                    ROCKETMQ_TOPIC,
                    "span",
                    message.getBytes()
            );
            System.out.println(message.getBytes());
            producer.send(msg);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        }catch (MQBrokerException e) {
            e.printStackTrace();
        }
    }

    public static void closeProducer() {
        producer.shutdown();
    }
}

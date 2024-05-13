package zql.CallRope.core.distruptor;

import zql.CallRope.core.config.Configuration;
import zql.CallRope.core.distruptor.model.DataEvent;
import zql.CallRope.core.rocketmq.SpanConsumer;
import zql.CallRope.core.rocketmq.SpanProducer;
import zql.CallRope.point.model.Span;
public class DisruptorConfig {
    private final static Integer MQ_COMSUMER_SIZE;
    private final static Integer DISRUPTOR_RING_BUFFER_SIZE;

    static {
        MQ_COMSUMER_SIZE = Configuration.getPropertyAsInteger("mq_comsumer_size");
        DISRUPTOR_RING_BUFFER_SIZE = Configuration.getPropertyAsInteger("disruptor_ring_buffer_size");
        SpanConsumer.start();
    }

    public static DataEventListener<Span> createConsumerListener() {
        DataEventListener<Span> dataEventListener = new DataEventListener<Span>() {
            @Override
            public void processDataEvent(DataEvent<Span> dataEvent) {
                SpanProducer.sendSpanToRocketMq(dataEvent.getData());
                dataEvent.clear();
            }
        };
        return dataEventListener;
    }


    public static DisruptorProducer<Span> createProducer(DataEventListener dataEventListener) {
        DisruptorManager disruptorManage = new DisruptorManager(dataEventListener,
                MQ_COMSUMER_SIZE,
                DISRUPTOR_RING_BUFFER_SIZE);
        disruptorManage.start();
        return disruptorManage.getProducer();
    }

}

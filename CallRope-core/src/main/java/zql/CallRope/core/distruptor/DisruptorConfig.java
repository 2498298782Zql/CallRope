package zql.CallRope.core.distruptor;

import zql.CallRope.core.distruptor.model.DataEvent;
import zql.CallRope.core.rocketmq.SpanConsumer;
import zql.CallRope.core.rocketmq.SpanProducer;
import zql.CallRope.point.model.Span;

public class DisruptorConfig {

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
                8,
                1024 * 1024);
        disruptorManage.start();
        SpanConsumer.start();
        return disruptorManage.getProducer();
    }


    private static String constructMessageFromSpan(Span span) {
        // 根据 Span 构造消息
        return "TraceId: " + span.traceId + ", SpanId: " + span.spanId + ", ServiceName: " + span.ServiceName + ", MethodName: " + span.MethodName;
    }
}

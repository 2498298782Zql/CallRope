package zql.CallRope.demo;

import zql.CallRope.core.distruptor.DisruptorConfig;
import zql.CallRope.core.distruptor.DisruptorProducer;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

public class DisruptorTest {
    static DisruptorProducer<Span> producer = DisruptorConfig.createProducer(DisruptorConfig.createConsumerListener());
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i =0 ;i< 10;i++){
                    producer.onData(new SpanBuilder(i+"", "2", "demp","demo","demo").build());
                }
            }
        }).start();
    }
}

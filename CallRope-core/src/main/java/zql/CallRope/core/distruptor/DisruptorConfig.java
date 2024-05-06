package zql.CallRope.core.distruptor;

import zql.CallRope.core.distruptor.model.DataEvent;
import zql.CallRope.point.model.Span;

public class DisruptorConfig {

    public static DataEventListener<Span> createConsumerListener(){
        DataEventListener<Span> dataEventListener = new DataEventListener<Span>() {
            @Override
            public void processDataEvent(DataEvent<Span> dataEvent) {
                uploadToKafka(dataEvent.getData());
            }
        };
        return dataEventListener;
    }


    public static DisruptorProducer<Span> createProducer(DataEventListener dataEventListener) {
        DisruptorManager disruptorManage = new DisruptorManager(dataEventListener,
                8,
                1024 * 1024 );
        disruptorManage.start();
        return disruptorManage.getProducer();
    }


    private static void uploadToKafka(Span span) {
        // 上传至Kafka的逻辑
        System.out.println("上传成功！ ->" + span.traceId);
    }

}

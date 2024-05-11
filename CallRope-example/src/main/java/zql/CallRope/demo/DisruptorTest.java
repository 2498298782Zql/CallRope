package zql.CallRope.demo;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import zql.CallRope.core.distruptor.DisruptorConfig;
import zql.CallRope.core.distruptor.DisruptorProducer;
import zql.CallRope.core.es.EsUtil;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.List;

public class DisruptorTest {
    static DisruptorProducer<Span> producer = DisruptorConfig.createProducer(DisruptorConfig.createConsumerListener());
    public static void main(String[] args) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i =11 ;i< 20;i++){
                    producer.onData(new SpanBuilder(i+"", "2", "demp","demo","demo").build());
                }
            }
        }).start();*/

        try {
            SearchResponse<Span> rope = EsUtil.queryAllByIndex("rope");
            List<Hit<Span>> hits = rope.hits().hits();
            hits.forEach( e -> System.out.println(e.source()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.core.distruptor.DisruptorConfig;
import zql.CallRope.core.distruptor.DisruptorProducer;
import zql.CallRope.point.model.Span;

import java.util.Map;

public class SpringFrameworkAspectImpl implements FrameworkAspect {

    private DisruptorProducer<Span> producer = DisruptorConfig.createProducer(DisruptorConfig.createConsumerListener());

    @Override
    public Object entry(Span span, Map<String, Object> infos) {
        span.start = System.currentTimeMillis();
        System.out.println(span);
        return span;
    }


    @Override
    public Object exit(Span span, Map<String, Object> infos) {
        span.end = System.currentTimeMillis();
        span.duration = span.end - span.start;
        producer.onData(span);
        return span;
    }
}

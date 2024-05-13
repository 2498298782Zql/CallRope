package zql.CallRope.core.aspect.aspectImpl;

import org.apache.kafka.common.utils.Sanitizer;
import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.core.distruptor.DisruptorConfig;
import zql.CallRope.core.distruptor.DisruptorProducer;
import zql.CallRope.point.Trace;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;
import zql.CallRope.point.model.SpanEnvironment;

import java.util.Map;

public class DubboProducerAspectImpl implements FrameworkAspect {

    private DisruptorProducer<Span> producer = DisruptorConfig.createProducer(DisruptorConfig.createConsumerListener());

    @Override
    public Object entry(Span span, Map<String, Object> infos) {
        span.start = System.currentTimeMillis();
        span.setEnv(SpanEnvironment.TEST_ENVIRONMENT);
        SpanBuilder.fix(span);
        Trace.spanTtl.set(span);
        return null;
    }

    @Override
    public Object exit(Span span, Map<String, Object> infos) {
        span.end = System.currentTimeMillis();
        span.duration = span.end - span.start;
        producer.onData(span);
        Trace.spanTtl.remove();
        return null;
    }
}

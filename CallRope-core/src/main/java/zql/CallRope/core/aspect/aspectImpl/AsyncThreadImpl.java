package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.AsyncThreadAspect;
import zql.CallRope.core.distruptor.DisruptorConfig;
import zql.CallRope.core.distruptor.DisruptorProducer;
import zql.CallRope.point.TraceInfos;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;
import zql.CallRope.point.model.SpanEnvironment;

import java.util.Map;
import java.util.Set;

public class AsyncThreadImpl implements AsyncThreadAspect {
    private DisruptorProducer<Span> producer = DisruptorConfig.createProducer(DisruptorConfig.createConsumerListener());

    @Override
    public void enter(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos) {
        Span span = new SpanBuilder(timeSpan.traceId, timeSpan.spanId, timeSpan.pspanId,
                timeSpan.serviceName, timeSpan.methodName).withStart(System.currentTimeMillis())
                .withIsAsyncThread(isAsyncThread)
                .withEnv(SpanEnvironment.PRODUCTION_ENVIRONMENT)
                .withMapLogInfos(infos).build();
        TraceInfos.spanTtl.set(span);
    }

    @Override
    public void exit(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos) {
        Span span = TraceInfos.spanTtl.get();
        span.end = System.currentTimeMillis();
        span.duration = span.end - span.start;
        Set<Map.Entry<String, Object>> entries = infos.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            span.logInfos.put(entry.getKey(), entry.getValue());
        }
        producer.onData(span);
        TraceInfos.spanTtl.remove();
    }
}

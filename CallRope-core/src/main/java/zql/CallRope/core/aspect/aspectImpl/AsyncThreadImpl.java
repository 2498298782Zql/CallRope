package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.AsyncThreadAspect;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;
import zql.CallRope.point.model.SpanEnvironment;

import java.util.Map;
import java.util.Set;

public class AsyncThreadImpl implements AsyncThreadAspect {
    public final static ThreadLocal<Span> SPAN_INFO_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void enter(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos) {
        Span span = new SpanBuilder(timeSpan.traceId, timeSpan.spanId, timeSpan.pspanId,
                timeSpan.ServiceName, timeSpan.MethodName).withStart(System.currentTimeMillis())
                .withIsAsyncThread(isAsyncThread).withEnv(SpanEnvironment.PRODUCTION_ENVIRONMENT)
                .withMapLogInfos(infos).build();
        SPAN_INFO_THREAD_LOCAL.set(span);
    }

    @Override
    public void exit(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos) {
        Span span = SPAN_INFO_THREAD_LOCAL.get();
        span.end = System.currentTimeMillis();
        span.duration = span.end - span.start;
        Set<Map.Entry<String, Object>> entries = infos.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            span.logInfos.put(entry.getKey(), entry.getValue());
        }
        System.out.println(span);
        SPAN_INFO_THREAD_LOCAL.remove();
    }
}

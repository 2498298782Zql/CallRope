package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.point.IDutils.TraceIdGenerator;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;
import zql.CallRope.point.model.SpanEnvironment;

import java.util.Map;

public class SpringFrameworkAspectImpl implements FrameworkAspect {
    public final static ThreadLocal<Span> SPAN_INFO_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void entry(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos) {
        if (traceId == null || traceId.trim().length() == 0) {
            traceId = TraceIdGenerator.generateTraceId();
        }
        if (spanId == null || spanId.trim().length() == 0) {
            spanId = "0";
        }
        if (parentSpanId == null || parentSpanId.trim().length() == 0) {
            parentSpanId = "-1";
        }
        Span span = new SpanBuilder(traceId, spanId, parentSpanId, serviceName, methodName).withMapLogInfos(infos)
                .withEnv(SpanEnvironment.PRODUCTION_ENVIRONMENT).withStart(System.currentTimeMillis()).build();
        SPAN_INFO_THREAD_LOCAL.set(span);
    }

    @Override
    public void exit(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos) {
        Span span = SPAN_INFO_THREAD_LOCAL.get();
        span.end = System.currentTimeMillis();
        span.duration = span.start - span.end;
        System.out.println(span);
    }

}

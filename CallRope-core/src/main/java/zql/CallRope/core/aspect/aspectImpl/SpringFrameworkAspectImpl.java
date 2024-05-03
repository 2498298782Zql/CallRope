package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.point.IDutils.TraceIdGenerator;
import zql.CallRope.point.TransmittableThreadLocal;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;
import zql.CallRope.point.model.SpanEnvironment;

import java.util.Map;

public class SpringFrameworkAspectImpl implements FrameworkAspect {
    public final static TransmittableThreadLocal<Span> SPAN_INFO_THREAD_LOCAL = new TransmittableThreadLocal<>();

    @Override
    public Span entry(Span span, Map<String, Object> infos) {
        SPAN_INFO_THREAD_LOCAL.set(span);
        return span;
    }


    @Override
    public Span exit(Span span, Map<String, Object> infos) {
        span.end = System.currentTimeMillis();
        span.duration = span.end - span.start;
        System.out.println(span);
        SPAN_INFO_THREAD_LOCAL.remove();
        return span;
    }
}

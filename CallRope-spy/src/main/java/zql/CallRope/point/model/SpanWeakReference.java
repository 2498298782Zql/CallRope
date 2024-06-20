package zql.CallRope.point.model;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class SpanWeakReference extends WeakReference<Span> {
    private String traceId;
    private String spanId;

    public SpanWeakReference(Span span, ReferenceQueue<? super Span> q) {
        super(span, q);
        this.traceId = span.traceId;
        this.spanId = span.spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

}

package zql.CallRope.point;

import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static zql.CallRope.point.TransmittableThreadLocal.Transmitter.*;

public class TtlCallable<V> implements TtlEnhanced, Callable<V> {
    private final AtomicReference<Object> capturedRef;
    private final Callable<V> callable;
    private final boolean releaseTtlValueReferenceAfterCall;

    private TtlCallable(Callable<V> callable, boolean releaseTtlValueReferenceAfterCall) {
        this.capturedRef = new AtomicReference<>(capture());
        this.callable = callable;
        this.releaseTtlValueReferenceAfterCall = releaseTtlValueReferenceAfterCall;
    }
    @Override
    public V call() throws Exception {
        final Object captured = capturedRef.get();
        if (captured == null || releaseTtlValueReferenceAfterCall && !capturedRef.compareAndSet(captured, null)) {
            throw new IllegalStateException("TTL value reference is released after call!");
        }
        final Object backup = replay(captured);
        try {
            Span oldSpan = (Span) Trace.spanTtl.get();
            if (oldSpan != null) {
                Span span = new SpanBuilder(oldSpan.traceId, oldSpan.spanId, oldSpan.pspanId, oldSpan.ServiceName, oldSpan.MethodName).withIsAsyncThread(true).build();
                Trace.spanTtl.set(span);
                SpyAPI.atFrameworkEnter(span, null);
            }
            V result = callable.call();
            oldSpan = Trace.spanTtl.get();
            if ( oldSpan != null) {
                SpyAPI.atFrameworkExit(oldSpan, null);
            }
            return result;
        } finally {
            restore(backup);
        }
    }
    public static <V> TtlCallable get(Callable<V> callable) {
        return create(callable, false);
    }

    public static <V> TtlCallable create(Callable<V> callable, boolean releaseTtlValueReferenceAfterRun) {
        if (null == callable) return null;
        if (callable instanceof TtlEnhanced) {
            throw new IllegalStateException("Already TtlCallable!");
        }
        return new TtlCallable(callable, releaseTtlValueReferenceAfterRun);
    }


}

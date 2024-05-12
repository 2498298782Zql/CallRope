package zql.CallRope.point;

import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

import java.util.concurrent.atomic.AtomicReference;

import static zql.CallRope.point.Trace.isThreadNameWithPrefix;
import static zql.CallRope.point.TransmittableThreadLocal.Transmitter.*;

public class TtlRunnable implements Runnable, TtlEnhanced {
    private final AtomicReference<Object> capturedRef;
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private Runnable runnable;
    private final boolean releaseTtlValueReferenceAfterRun;

    private TtlRunnable(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
        this.capturedRef = new AtomicReference<>(capture());
        this.runnable = runnable;
        this.releaseTtlValueReferenceAfterRun = releaseTtlValueReferenceAfterRun;
    }



    @Override
    public void run() {
        if (isThreadNameWithPrefix()) {
            runnable.run();
            return;
        }
        Object captured = capturedRef.get();
        if (captured == null && releaseTtlValueReferenceAfterRun && !capturedRef.compareAndSet(captured, null)) {
            throw new IllegalStateException("TTL value reference is released after run!");
        }
        Object backup = replay(captured);
        try {
            Span oldSpan = (Span) Trace.spanTtl.get();
            // 真正的Runnable调用
            if (oldSpan != null) {
                Span span = new SpanBuilder(oldSpan.traceId, oldSpan.spanId, oldSpan.pspanId, oldSpan.ServiceName, oldSpan.MethodName).withIsAsyncThread(true).build();
                Trace.spanTtl.set(span);
                SpyAPI.atFrameworkEnter(span, null, new String[]{"SpringFrameworkAspectImpl"});
            }
            runnable.run();
            oldSpan = Trace.spanTtl.get();
            if ( oldSpan != null) {
                SpyAPI.atFrameworkExit(oldSpan, null, new String[]{"SpringFrameworkAspectImpl"});
            }
        } finally {
            restore(backup);
        }
    }

    public static TtlRunnable get(Runnable runnable) {
        return create(runnable, false);
    }

    public static TtlRunnable create(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
        if (null == runnable) return null;
        if (runnable instanceof TtlEnhanced) {
            throw new IllegalStateException("Already TtlRunnable!");
        }
        return new TtlRunnable(runnable, releaseTtlValueReferenceAfterRun);
    }

}
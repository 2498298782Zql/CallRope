package zql.CallRope.point;

import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;
import static zql.CallRope.point.TransmittableThreadLocal.Transmitter.*;

public class TtlRunnable implements Runnable, TtlEnhanced {
    private final AtomicReference<Object> capturedRef;
    private final Deliverthreadlocal deliverthreadlocal = new Deliverthreadlocal();
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

    private static String tomcatThreadNamePrefix = "http-nio";

    @Override
    public void run() {
        if(Thread.currentThread().getName().contains(tomcatThreadNamePrefix)){
            runnable.run();
            return;
        }
        Object captured = capturedRef.get();
        if (captured == null && releaseTtlValueReferenceAfterRun && !capturedRef.compareAndSet(captured, null)) {
            throw new IllegalStateException("TTL value reference is released after run!");
        }
        Object backup = replay(captured, deliverthreadlocal);
        try {
            Span oldSpan = null;
            Span span = null;
            // 真正的Runnable调用
            if (deliverthreadlocal.spanThreadLocal != null) {
                System.out.println(Thread.currentThread().getName());
                oldSpan = (Span) deliverthreadlocal.spanThreadLocal.get();
                span = new SpanBuilder(oldSpan.traceId, oldSpan.spanId, oldSpan.pspanId, oldSpan.ServiceName, oldSpan.MethodName).withIsAsyncThread(true).build();
                SpyAPI.atFrameworkEnter(span, null);
            }
            runnable.run();
            if(span != null) {
                SpyAPI.atFrameworkExit(span, null);
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
package zql.CallRope.point;

import zql.CallRope.point.model.Span;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;

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
        Object captured = capturedRef.get();
        if (captured == null && releaseTtlValueReferenceAfterRun && !capturedRef.compareAndSet(captured, null)) {
            throw new IllegalStateException("TTL value reference is released after run!");
        }
        Object backup = replay(captured);
        try {
            // 真正的Runnable调用
            runnable.run();
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


    /*TtlRunnable ttlRunnable = new TtlRunnable(runnable, releaseTtlValueReferenceAfterRun);
    Snapshot snapshot = (Snapshot) ttlRunnable.capturedRef.get();
    WeakHashMap<TransmittableThreadLocal<Object>, Object> ttl2Value = snapshot.ttl2Value;
    Set<Map.Entry<TransmittableThreadLocal<Object>, Object>> entries = ttl2Value.entrySet();
    TransmittableThreadLocal<Object> transmittableThreadLocal = null;
        for (Map.Entry<TransmittableThreadLocal<Object>, Object> entry : entries) {
        if (entry.getValue() instanceof Span) {
            transmittableThreadLocal = entry.getKey();
            System.out.println("hello");
            break;
        }
    }
    TransmittableThreadLocal<Object> threadLocal = transmittableThreadLocal;
        ttlRunnable.capturedRef.set(capture());
    final Runnable finalRunnable = new Runnable() {
        @Override
        public void run() {
            if(threadLocal == null){
                System.out.println("real is null");
            }
            runnable.run();
        }
    };
        ttlRunnable.setRunnable(finalRunnable);
        return ttlRunnable;*/
}
package zql.CallRope.point;

import java.util.concurrent.atomic.AtomicReference;

import static zql.CallRope.point.TransmittableThreadLocal.Transmitter.*;

public class TtlRunnable implements Runnable, TtlEnhanced {
    private final AtomicReference<Object> capturedRef;
    private final Runnable runnable;
    private final boolean releaseTtlValueReferenceAfterRun;

    public TtlRunnable(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
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
        return get(runnable, false);
    }

    public static TtlRunnable get(Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
        if (null == runnable) return null;
        if (runnable instanceof TtlEnhanced) {
            throw new IllegalStateException("Already TtlRunnable!");
        }
        return new TtlRunnable(runnable, releaseTtlValueReferenceAfterRun);
    }
}

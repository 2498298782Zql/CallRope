package zql.CallRope.point.model;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SpanMonitor {
    private static ReferenceQueue<Span> referenceQueue;
    private ExecutorService executorService;

    public SpanMonitor() {
        this.referenceQueue = new ReferenceQueue<>();
        this.executorService = Executors.newSingleThreadExecutor();
        startMonitoring();
    }

    public ReferenceQueue<Span> getReferenceQueue() {
        return referenceQueue;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    private void startMonitoring() {
        executorService.execute(() -> {
            while (true) {
                try {
                    SpanWeakReference ref = (SpanWeakReference) referenceQueue.remove();
                    // TODO 发送至mq
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

}


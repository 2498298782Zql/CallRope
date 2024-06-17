package zql.CallRope.point;

import zql.CallRope.point.model.Span;
import zql.CallRope.point.threadpool.TransmittableThreadLocal;

import java.util.Set;

import static zql.CallRope.point.config.Configuration.getPropertyAsSet;

public class TraceInfos {
    public static final TransmittableThreadLocal<Span> spanTtl = new TransmittableThreadLocal<>();
    public static final Set<String> threadPrefixSet;

    static {
        threadPrefixSet = getPropertyAsSet("threadpool-name-prefix");
    }

    public static final boolean isThreadNameWithPrefix() {
        String currentThreadName = Thread.currentThread().getName();
        return threadPrefixSet.stream().anyMatch(currentThreadName::startsWith);
    }
}

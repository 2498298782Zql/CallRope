package zql.CallRope.core.aspect;

public interface FrameworkAspect {
    /**
     * 调用链路入口监听
     */
    void entry(String traceId, String spanId, String parentSpanId);

    /**
     * 调用链路出口监听
     */
    void exit(String info);
}

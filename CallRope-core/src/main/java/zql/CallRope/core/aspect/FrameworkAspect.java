package zql.CallRope.core.aspect;

import java.util.Map;

public interface FrameworkAspect {
    /**
     * 调用链路入口监听
     */
    void entry(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos);

    /**
     * 调用链路出口监听
     */
    void exit(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos);
}

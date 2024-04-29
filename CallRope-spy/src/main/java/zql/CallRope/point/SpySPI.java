package zql.CallRope.point;

import java.util.Map;

public interface SpySPI {
    public void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos);

    public void atExit(Class<?> clazz, String methodInfo, Object target, Object returnObject, Map<String, Object> infos);

    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos, Throwable throwable);

    public void atFrameworkEnter(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos);

    public void atFrameworkExit(String traceId, String spanId, String parentSpanId, String serviceName, String methodName, Map<String, Object> infos);
}

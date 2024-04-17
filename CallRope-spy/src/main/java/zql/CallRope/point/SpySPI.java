package zql.CallRope.point;

public interface SpySPI {
    public void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args);

    public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject);

    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable);

    public void atFrameworkEnter(String traceId, String spanId, String parentSpanId);

    public void atFrameworkExit(String info);
}

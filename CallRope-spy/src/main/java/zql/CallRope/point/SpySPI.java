package zql.CallRope.point;

import zql.CallRope.point.model.Span;

import java.util.Map;

public interface SpySPI {
    public void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos);

    public void atExit(Class<?> clazz, String methodInfo, Object target, Object returnObject, Map<String, Object> infos);

    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos, Throwable throwable);

    public void atFrameworkEnter(Span span, Map<String, Object> infos);

    public void atFrameworkExit(Span span, Map<String, Object> infos);

    public void atFrameThreadPoolEnter(Span span);

    public void atFrameThreadPoolExit(Span span);
}

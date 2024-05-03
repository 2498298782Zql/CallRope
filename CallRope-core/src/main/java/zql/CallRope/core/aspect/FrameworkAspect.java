package zql.CallRope.core.aspect;

import zql.CallRope.point.model.Span;

import java.util.Map;

public interface FrameworkAspect {
    /**
     * 调用链路入口监听
     */
    Object entry(Span span, Map<String, Object> infos);

    /**
     * 调用链路出口监听
     */
    Object exit(Span span, Map<String, Object> infos);
}

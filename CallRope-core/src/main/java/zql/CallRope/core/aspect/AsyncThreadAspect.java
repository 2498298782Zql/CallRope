package zql.CallRope.core.aspect;

import zql.CallRope.point.model.Span;

import java.util.Map;

public interface AsyncThreadAspect {
    void enter(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos);
    void exit(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos);
}

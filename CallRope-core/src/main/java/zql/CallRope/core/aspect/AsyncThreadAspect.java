package zql.CallRope.core.aspect;

import zql.CallRope.point.model.Span;
import zql.CallRope.spi.annotation.SPI;

import java.util.Map;

@SPI
public interface AsyncThreadAspect {
    void enter(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos);
    void exit(Span timeSpan, boolean isAsyncThread, Map<String, Object> infos);
}

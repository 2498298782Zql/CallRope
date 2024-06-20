package zql.CallRope.point.log.Filter;

import zql.CallRope.point.log.LifeCycle;
import zql.CallRope.point.log.LoggingEvent;

public interface Filter extends LifeCycle {
    boolean doFilter(LoggingEvent event);
}

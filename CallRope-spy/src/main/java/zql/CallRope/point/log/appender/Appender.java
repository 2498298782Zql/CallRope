package zql.CallRope.point.log.appender;

import zql.CallRope.point.log.LifeCycle;
import zql.CallRope.point.log.LoggingEvent;

/**
 * 日志输出方式
 */
public interface Appender extends LifeCycle {
    void append(LoggingEvent event);

    String getName();

    void setName(String name);
}

package zql.CallRope.point.log.appender;

import zql.CallRope.point.log.LoggingEvent;

/**
 * 日志输出方式
 */
public interface Appender {
    void append(LoggingEvent event);

    String getName();

    void setName(String name);
}

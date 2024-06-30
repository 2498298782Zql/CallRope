package zql.CallRope.point.log.layout;

import zql.CallRope.point.log.LifeCycle;
import zql.CallRope.point.log.LoggingEvent;

/**
 * 按xml配置的模板 格式化字符串
 */
public interface Layout extends LifeCycle {
    String doLayout(LoggingEvent event);
}

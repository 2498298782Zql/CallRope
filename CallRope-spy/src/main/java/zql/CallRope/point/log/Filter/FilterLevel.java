package zql.CallRope.point.log.Filter;

import zql.CallRope.point.log.LoggingEvent;
import zql.CallRope.point.log.appender.Level;

public class FilterLevel implements Filter{


    // 从配置文件读取
    private String levelStr;

    // 通过level自动转化
    private Level level;


    @Override
    public boolean doFilter(LoggingEvent event) {
        return event.getLevel().isGreaterOrEqual(level);
    }

    @Override
    public void start() {
        this.level = Level.parse(levelStr);
    }


    @Override
    public void stop() {}
}

package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.LoggingEvent;

public class ThreadConverter extends KeywordConverter {
    @Override
    public String convert(LoggingEvent e) {
        return e.getThreadName();
    }
}


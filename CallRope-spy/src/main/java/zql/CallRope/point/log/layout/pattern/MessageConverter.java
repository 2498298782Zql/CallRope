package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.LoggingEvent;

public class MessageConverter extends KeywordConverter {
    @Override
    public String convert(LoggingEvent e) {
        return String.valueOf(e.getMessage());
    }
}

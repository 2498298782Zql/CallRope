package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.LoggingEvent;

public interface Converter {
    String convert(LoggingEvent e);
}

package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.LoggingEvent;

public class LevelConverter implements Converter {
    @Override
    public String convert(LoggingEvent e) {
        return e.getLevel().toString();
    }
}

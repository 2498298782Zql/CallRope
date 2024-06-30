package zql.CallRope.point.log.layout.pattern;

import zql.CallRope.point.log.LoggingEvent;

public class LiteralConverter implements Converter {
    private String literal;

    @Override
    public String convert(LoggingEvent e) {
        return literal;
    }

    public LiteralConverter(String literal) {
        this.literal = literal;
    }
}

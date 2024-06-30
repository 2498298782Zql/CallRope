package zql.CallRope.point.log.layout;

import zql.CallRope.point.log.LoggingEvent;

public class PlainLayout implements Layout{
    @Override
    public String doLayout(LoggingEvent e) {
        return e.toString();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

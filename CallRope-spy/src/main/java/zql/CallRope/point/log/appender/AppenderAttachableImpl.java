package zql.CallRope.point.log.appender;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppenderAttachableImpl implements AppenderAttachable {

    List<Appender> appenderList = new CopyOnWriteArrayList<>();

    @Override
    public void addAppender(Appender newAppender) {

    }

    @Override
    public Appender getAppender(String name) {
        return null;
    }

    @Override
    public boolean isAttached(Appender appender) {
        return false;
    }

    @Override
    public void removeAppender(Appender appender) {

    }

    @Override
    public void removeAppender(String name) {

    }
}

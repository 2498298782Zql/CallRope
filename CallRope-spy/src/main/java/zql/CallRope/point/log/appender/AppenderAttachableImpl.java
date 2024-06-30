package zql.CallRope.point.log.appender;

import zql.CallRope.point.log.LoggingEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppenderAttachableImpl implements AppenderAttachable {

    List<Appender> appenderList = new CopyOnWriteArrayList<>();

    @Override
    public void addAppender(Appender newAppender) {
        appenderList.add(newAppender);
    }

    public int appendLoopOnAppenders(LoggingEvent event) {
        int size = 0;
        Appender appender;
        // 一个logger可能同时有多个输出器
        if (appenderList != null) {
            size = appenderList.size();
            for (int i = 0; i < size; i++) {
                appender = appenderList.get(i);
                appender.append(event);
            }
        }
        return size;
    }

    @Override
    public Appender getAppender(String name) {
        if (name == null) {
            return null;
        }
        for (Appender appender : appenderList) {
            if (name.equals(appender.getName())) {
                return appender;
            }
        }
        return null;
    }

    @Override
    public boolean isAttached(Appender appender) {
        if (appender == null) {
            return false;
        }
        for (Appender a : appenderList) {
            if (a == appender)
                return true;
        }
        return false;
    }

    @Override
    public void removeAppender(Appender appender) {
        removeAppender(appender.getName());
    }

    @Override
    public void removeAppender(String name) {
        if (name == null) {
            return ;
        }
        for (Appender a : appenderList) {
            if (name.equals((a).getName())) {
                appenderList.remove(a);
                break;
            }
        }
    }
}

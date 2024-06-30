package zql.CallRope.point.log.appender;

/**
 * appender 输出器管理工具
 */
public interface AppenderAttachable {
    void addAppender(Appender newAppender);

    Appender getAppender(String name);

    boolean isAttached(Appender appender);

    void removeAppender(Appender appender);

    void removeAppender(String name);
}

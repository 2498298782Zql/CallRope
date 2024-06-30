package zql.CallRope.point.log;

import zql.CallRope.point.log.appender.Level;

/**
 * 基本事件
 */
public class LoggingEvent {
    public long timestamp;//日志时间戳
    private Level level;//日志级别
    private Object message;//日志主题
    private String threadName;//线程名称
    private long threadId;//线程id
    private String loggerName;//日志名称

    public LoggingEvent(Level level, Object message, String loggerName) {
        this.level = level;
        this.message = message;
        this.loggerName = loggerName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public String toString() {
        return "LoggingEvent{" +
                "timestamp=" + timestamp +
                ", level=" + level +
                ", message=" + message +
                ", threadName='" + threadName + '\'' +
                ", threadId=" + threadId +
                ", loggerName='" + loggerName + '\'' +
                '}';
    }
}

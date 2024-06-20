package zql.CallRope.point.log.appender;

/**
 * 日志级别
 * ERROR > WARN > INFO > DEBUG > TRACE
 */
public enum Level {
    TRACE(5000, "TRACE"),
    DEBUG(10000, "DEBUG"),
    INFO(20000, "INFO"),
    WARN(30000, "WARN"),
    ERROR(40000, "ERROR");

    private int levelInt;
    private String levelStr;

    Level(int levelInt, String levelStr) {
        this.levelInt = levelInt;
        this.levelStr = levelStr;
    }

    public static Level parse(String level) {
        return valueOf(level.toUpperCase());
    }

    public int toInt() {
        return levelInt;
    }

    public String toString() {
        return levelStr;
    }

    public boolean isGreaterOrEqual(Level level) {
        return levelInt>=level.toInt();
    }

}

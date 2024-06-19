package zql.CallRope.point.log;

public interface Logger {
    void trace(String msg);

    void info(String msg);

    void debug(String msg);

    void warn(String msg);

    void error(String msg);


    String getName();
}

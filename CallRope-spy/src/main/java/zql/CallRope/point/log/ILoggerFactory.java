package zql.CallRope.point.log;

public interface ILoggerFactory {
    //通过class获取/创建logger
    Logger getLogger(Class<?> clazz);
    //通过name获取/创建logger
    Logger getLogger(String name);
    //通过name创建logger
    Logger newLogger(String name);
}

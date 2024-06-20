package zql.CallRope.point.log;

public class LoggerFactory {
    private static ILoggerFactory loggerFactory = new StaticLoggerFactory();

    public static ILoggerFactory getLoggerFactory(){
        return loggerFactory;
    }

    public static Logger getLogger(Class<?> clazz){
        return loggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name){
        return loggerFactory.getLogger(name);
    }

}

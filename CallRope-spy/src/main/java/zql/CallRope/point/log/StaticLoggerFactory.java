package zql.CallRope.point.log;

public class StaticLoggerFactory implements ILoggerFactory {

    private LoggerContext loggerContext;//引用LoggerContext

    @Override
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    @Override
    public Logger getLogger(String name) {
        Logger logger = loggerContext.getLoggerCache().get(name);
        if(logger == null){
            logger = newLogger(name);
        }
        return logger;
    }

    /**
     * 创建Logger对象
     * 匹配logger name，拆分类名后和已创建（包括配置的）的Logger进行匹配
     * 比如当前name为com.aaa.bbb.ccc.XXService，那么name为com/com.aaa/com.aaa.bbb/com.aaa.bbb.ccc
     * 的logger都可以作为parent logger，不过这里需要顺序拆分，优先匹配“最近的”
     * 在这个例子里就会优先匹配com.aaa.bbb.ccc这个logger，作为自己的parent
     *
     * 如果没有任何一个logger匹配，那么就使用root logger作为自己的parent
     *
     * @param name Logger name
     */
    @Override
    public Logger newLogger(String name) {
        Logc logger = new Logc();
        logger.setName(name);
        Logger parent = null;
        //拆分包名，向上查找parent logger
        for (int i = name.lastIndexOf("."); i >= 0; i = name.lastIndexOf(".",i-1)) {
            String parentName = name.substring(0,i);
            parent = loggerContext.getLoggerCache().get(parentName);
            if(parent != null){
                break;
            }
        }
        if(parent == null){
            parent = loggerContext.getRoot();
        }
        logger.setParent(parent);
        logger.setLoggerContext(loggerContext);
        return logger;
    }
}

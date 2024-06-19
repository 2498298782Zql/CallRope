package zql.CallRope.point.log;

import java.util.HashMap;
import java.util.Map;

public class LoggerContext {
    private Logger root;

    private Map<String,Logger> loggerCache = new HashMap<>();

    public void addLogger(String name,Logger logger){
        loggerCache.put(name,logger);
    }

    public void addLogger(Logger logger){
        loggerCache.put(logger.getName(),logger);
    }

    public Logger getRoot() {
        return root;
    }

    public void setRoot(Logger root) {
        this.root = root;
    }

    public Map<String, Logger> getLoggerCache() {
        return loggerCache;
    }

    public void setLoggerCache(Map<String, Logger> loggerCache) {
        this.loggerCache = loggerCache;
    }
}

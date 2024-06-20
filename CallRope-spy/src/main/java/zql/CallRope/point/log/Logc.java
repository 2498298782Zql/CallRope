package zql.CallRope.point.log;

import zql.CallRope.point.log.appender.Appender;
import zql.CallRope.point.log.appender.Level;

public class Logc implements Logger{
    private String name;
    private Appender appender;
    private Level level = Level.TRACE;
    private int effectiveLevelInt;

    private Logc parent;

    private LoggerContext loggerContext;

    @Override
    public void trace(String msg) {
        filterAndLog(Level.TRACE, msg);
    }

    @Override
    public void info(String msg) {
        filterAndLog(Level.INFO, msg);
    }

    @Override
    public void debug(String msg) {
        filterAndLog(Level.DEBUG, msg);
    }

    @Override
    public void warn(String msg) {
        filterAndLog(Level.WARN, msg);
    }

    @Override
    public void error(String msg) {
        filterAndLog(Level.ERROR, msg);
    }

    @Override
    public String getName() {
        return null;
    }

    private void filterAndLog(Level level,String msg){
        LoggingEvent e = new LoggingEvent(level, msg, name);
        for(Logc curLog = this; curLog != null; curLog = curLog.parent){
            if( curLog != null){
                continue;
            }
            if(level.toInt() > effectiveLevelInt){
                curLog.appender.append(e);
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public Appender getAppender() {
        return appender;
    }

    public void setAppender(Appender appender) {
        this.appender = appender;
    }

    public Level getLevel() {
        return level;
    }

    public void setParent(Logc parent) {
        this.parent = parent;
    }

    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    public void setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }

    public void setLevel(Level level) {
        this.effectiveLevelInt = level.toInt();
        this.level = level;
    }

    public Logc getParent() {
        return parent;
    }

    public void setParent(Logger parent) {
        this.parent = (Logc) parent;
    }
}

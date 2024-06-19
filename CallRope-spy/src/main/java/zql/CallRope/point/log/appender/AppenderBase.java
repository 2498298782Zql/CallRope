package zql.CallRope.point.log.appender;

import zql.CallRope.point.log.Filter.Filter;
import zql.CallRope.point.log.LoggingEvent;
import zql.CallRope.point.log.layout.Layout;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 采用模板方法优化代码
 * 每个appender对应一个appenderBase
 */
public abstract class AppenderBase implements Appender {
    // 格式化模板
    protected Layout layout;

    //过滤条件列表
    protected List<Filter> filterList;

    // 输出器名字
    protected String name;

    protected Object lock = new Object();


    protected String encoding = Charset.defaultCharset().name();

    @Override
    public void append(LoggingEvent event) {
        if (filterList != null) {
            for (Filter filter : filterList) {
                if (!filter.doFilter(event)) {
                    return;
                }
            }
        }
        // 格式化字符串
        String body = layout.doLayout(event);
        synchronized (lock) {
            // 交由实际的输出器(方式：文件, 控制台......)输出
            doAppend(body);
        }
    }

    protected abstract void doAppend(String body);


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}

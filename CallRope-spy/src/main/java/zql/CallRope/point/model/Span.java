package zql.CallRope.point.model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Span {
    public final transient AtomicInteger nextId = new AtomicInteger(0);// 用于同一层级的spanId自增
    public String traceId;
    public String spanId;
    public String pspanId;
    public String ServiceName;  // appName
    public String MethodName;
    public SpanEnvironment env;
    public long start;
    public long end;
    public long duration; // end - start
    public Map<String, Object> logInfos;


    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName, SpanEnvironment env,
                   long start, long end, long duration, Map<String, Object> LogInfos) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.pspanId = pspanId;
        ServiceName = serviceName;
        MethodName = methodName;
        this.env = env;
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.logInfos = logInfos;
    }

    // 四个必要属性 + nextID;
    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.pspanId = pspanId;
        ServiceName = serviceName;
        MethodName = methodName;
    }
}




package zql.CallRope.core.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Span {
    final transient AtomicInteger nextId = new AtomicInteger(0);// 用于同一层级的spanId自增
    String traceId;
    String spanId;
    String ServiceName;  // appName
    String MethodName;
    SpanEnvironment env;
    long start;
    long end;
    long TakeTime; // end - start
    List<LogInfo> LogInfos;


    protected Span(String traceId, String spanId, String serviceName, String methodName, SpanEnvironment env,
                   long start, long end, long takeTime, List<LogInfo> logInfos) {
        this.traceId = traceId;
        this.spanId = spanId;
        ServiceName = serviceName;
        MethodName = methodName;
        this.env = env;
        this.start = start;
        this.end = end;
        TakeTime = takeTime;
        LogInfos = logInfos;
    }

    // 四个必要属性 + nextID;
    protected Span(String traceId, String spanId, String serviceName, String methodName) {
        this.traceId = traceId;
        this.spanId = spanId;
        ServiceName = serviceName;
        MethodName = methodName;
    }
}




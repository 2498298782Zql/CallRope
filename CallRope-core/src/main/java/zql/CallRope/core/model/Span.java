package zql.CallRope.core.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Span {
    final transient AtomicInteger nextId = new AtomicInteger(0);//用于同一层级的spanId自增
    String traceId;
    String spanId;
    SpanEnvironment type;
    long start;
    long end;
    long TakeTime; // end - start
    List<LogInfo> LogInfos;

    public Span(String traceId, String spanId, SpanEnvironment type,
                long start, long end, long takeTime) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.type = type;
        this.start = start;
        this.end = end;
        TakeTime = takeTime;
        LogInfos = new LinkedList<LogInfo>();
    }
}




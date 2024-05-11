package zql.CallRope.point.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Span implements Serializable{
    private final transient AtomicInteger nextId = new AtomicInteger(0);// 用于同一层级的spanId自增
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
    public Boolean isAsyncThread;


    private Span() {
    }


    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName,
                   SpanEnvironment env, long start, long end, long duration, Map<String, Object> logInfos) {
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

    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName,
                SpanEnvironment env, long start, long end, long duration, Map<String, Object> logInfos, Boolean isAsyncThread) {
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
        this.isAsyncThread = isAsyncThread;
    }

    // 四个必要属性 + nextID;
    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.pspanId = pspanId;
        ServiceName = serviceName;
        MethodName = methodName;
    }

    @Override
    public String toString() {
        return "Span{" +
                "traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", pspanId='" + pspanId + '\'' +
                ", ServiceName='" + ServiceName + '\'' +
                ", MethodName='" + MethodName + '\'' +
                ", env=" + env +
                ", start=" + start +
                ", end=" + end +
                ", duration=" + duration +
                ", logInfos=" + logInfos +
                ", isAsyncThread=" + isAsyncThread +
                '}';
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getPspanId() {
        return pspanId;
    }

    public void setPspanId(String pspanId) {
        this.pspanId = pspanId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public SpanEnvironment getEnv() {
        return env;
    }

    public void setEnv(SpanEnvironment env) {
        this.env = env;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Map<String, Object> getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(Map<String, Object> logInfos) {
        this.logInfos = logInfos;
    }

    public Boolean getAsyncThread() {
        return isAsyncThread;
    }

    public void setAsyncThread(Boolean asyncThread) {
        isAsyncThread = asyncThread;
    }
}




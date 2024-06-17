package zql.CallRope.point.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Span implements Serializable{
    private final transient AtomicInteger nextId = new AtomicInteger(1);// 用于同一层级的spanId自增

    public String traceId;
    public String spanId;
    public String pspanId;
    public String serviceName;  // appName

    public String methodName;
    public SpanEnvironment env;

    public long start;
    public long end;
    public long duration; // end - start
    public Map<String, Object> logInfos;
    public Boolean isAsyncThread; // 标志是否时线程池

    private Span() {
    }

    public Integer LevelSpanId(){
        return nextId.getAndIncrement();
    }

    protected Span(String traceId, String spanId, String pspanId, String serviceName, String methodName,
                   SpanEnvironment env, long start, long end, long duration, Map<String, Object> logInfos) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.pspanId = pspanId;
        this.serviceName = serviceName;
        this.methodName = methodName;
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
        this.serviceName = serviceName;
        this.methodName = methodName;
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
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Span{" +
                "traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", pspanId='" + pspanId + '\'' +
                ", ServiceName='" + serviceName + '\'' +
                ", MethodName='" + methodName + '\'' +
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
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public Boolean getIsAsyncThread() {
        return isAsyncThread;
    }

    public void setIsAsyncThread(Boolean asyncThread) {
        isAsyncThread = asyncThread;
    }
}




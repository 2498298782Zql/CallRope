package zql.CallRope.point.model;

import java.util.Map;

// 建造者模式
public class SpanBuilder {
    private Span span;

    public SpanBuilder(String traceId, String spanId, String pspanId, String serviceName, String methodName) {
        span = new Span(traceId, spanId, pspanId, serviceName, methodName);
    }

    public SpanBuilder(String traceId, String spanId, String pspanId, String serviceName, String methodName, SpanEnvironment env,
                       long start, long end, long duration, Map<String, Object> logInfos) {
        span = new Span(traceId, spanId, pspanId, serviceName, methodName, env, start, end, duration, logInfos);
    }

    public SpanBuilder(String traceId, String spanId, String pspanId, String serviceName, String methodName, SpanEnvironment env,
                       long start, long end, long duration, Map<String, Object> logInfos, boolean isAsyncThread){
        span = new Span(traceId, spanId, pspanId, serviceName, methodName, env, start, end, duration, logInfos, isAsyncThread);
    }

    // 可选属性
    public SpanBuilder withPspanId(String pspanId) {
        span.pspanId = pspanId;
        return this;
    }


    public SpanBuilder withEnv(SpanEnvironment env) {
        span.env = env;
        return this;
    }

    public SpanBuilder withStart(long start) {
        span.start = start;
        return this;
    }

    public SpanBuilder withEnd(long end) {
        span.end = end;
        return this;
    }

    public SpanBuilder withMapLogInfos(Map<String, Object> logInfos) {
        span.logInfos = logInfos;
        return this;
    }

    public SpanBuilder withDuration(long duration) {
        span.duration = duration;
        return this;
    }

    public SpanBuilder withIsAsyncThread(boolean isAsyncThread){
        span.isAsyncThread = isAsyncThread;
        return this;
    }

    public Span build() {
        return span;
    }

    public static void fix(Span span){
        if(span.spanId == null || span.spanId.trim() == ""){
            span.spanId = "1";
        }
        if(span.pspanId == null){
            span.pspanId = "null";
        }
    }
}

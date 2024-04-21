package zql.CallRope.core.model;

import java.util.List;

// 建造者模式
public class SpanBuilder {
    private Span span;

    public SpanBuilder(String traceId, String spanId, String serviceName, String methodName) {
        span = new Span(traceId, spanId, serviceName, methodName);
    }

    public SpanBuilder(String traceId, String spanId, String serviceName, String methodName, SpanEnvironment env,
                       long start, long end, long takeTime, List<LogInfo> logInfos) {
        span = new Span(traceId, spanId, serviceName, methodName, env, start, end, takeTime, logInfos);
    }

    // 可选属性
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

    public SpanBuilder withListLogInfos(List<LogInfo> logInfos) {
        span.LogInfos = logInfos;
        return this;
    }

    public Span build() {
        return span;
    }
}

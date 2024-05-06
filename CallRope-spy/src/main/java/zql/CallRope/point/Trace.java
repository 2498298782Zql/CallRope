package zql.CallRope.point;

import zql.CallRope.point.model.Span;

import java.util.HashSet;
import java.util.Set;

public class Trace {
    public static final TransmittableThreadLocal<Span> spanTtl = new TransmittableThreadLocal<Span>();
    public static final Set<String> threadPrefixSet = new HashSet<String>();
    public static final String tomcatThreadNamePrefix = "http";
    public static final String dubboThreadNamePrefix = "DubboServerHandler";

    static {
        // 添加你要检查的前缀到集合中
        threadPrefixSet.add(tomcatThreadNamePrefix);
        threadPrefixSet.add(dubboThreadNamePrefix);
    }

    public static final boolean isThreadNameWithPrefix() {
        String currentThreadName = Thread.currentThread().getName();
        return threadPrefixSet.stream().anyMatch(currentThreadName::startsWith);
    }
}

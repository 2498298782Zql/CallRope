package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.FrameworkAspect;

import java.util.Map;

public class SpringFrameworkAspectImpl implements FrameworkAspect {
    @Override
    public void entry(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        System.out.println("entry");
    }

    @Override
    public void exit(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        System.out.println("exit,kuazhangle");
    }
}

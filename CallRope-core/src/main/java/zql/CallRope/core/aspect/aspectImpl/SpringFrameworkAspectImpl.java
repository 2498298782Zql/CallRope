package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.FrameworkAspect;

public class SpringFrameworkAspectImpl implements FrameworkAspect {
    @Override
    public void entry(String traceId, String spanId, String parentSpanId) {
        System.out.println("entry");
    }

    @Override
    public void exit(String info) {
        System.out.println("exit");
    }
}

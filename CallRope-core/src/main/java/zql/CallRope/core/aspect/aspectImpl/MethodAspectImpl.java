package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.MethodAspect;
import zql.CallRope.core.model.Span;

import java.util.HashMap;
import java.util.Map;


public class MethodAspectImpl implements MethodAspect {

    private Map<String, Span> callRecordMap = new HashMap<>();

    @Override
    public void before(Class<?> clazz, String methodName, String methodDesc, Object target, Map<String,Object> infos) throws Throwable {
        System.out.println("before");
    }

    @Override
    public void after(Class<?> clazz, String methodName, String methodDesc, Object target, Object returnObject, Map<String,Object> infos) throws Throwable {
        System.out.println("after");
    }

    @Override
    public void error(Class<?> clazz, String methodName, String methodDesc, Object target, Map<String,Object> infos, Throwable throwable) throws Throwable {
        System.out.println("error");
    }
}

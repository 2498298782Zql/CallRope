package aspect.aspectImpl;

import aspect.MethodAspect;
import model.Span;

import java.util.HashMap;
import java.util.Map;


public class MethodAspectImpl implements MethodAspect {

    private Map<String, Span> callRecordMap = new HashMap<>();

    @Override
    public void before(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args) throws Throwable {

    }

    @Override
    public void after(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Object returnObject) throws Throwable {

    }

    @Override
    public void error(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Throwable throwable) throws Throwable {

    }
}

package zql.CallRope.core.aspect.aspectImpl;

import zql.CallRope.core.aspect.MethodAspect;
import java.util.Map;


/**
 * 加强普通方法
 */
public class MethodAspectImpl implements MethodAspect {
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

package zql.CallRope.core.aspect;

public interface MethodAspect{
    void before(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args) throws Throwable;


    void after(
            Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Object returnObject)
            throws Throwable;

    void error(
            Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Throwable throwable)
            throws Throwable;
}

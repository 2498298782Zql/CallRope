package zql.CallRope.core.aspect;

import zql.CallRope.spi.annotation.SPI;

import java.util.Map;

@SPI
public interface MethodAspect{
    void before(Class<?> clazz, String methodName, String methodDesc, Object target, Map<String,Object> infos) throws Throwable;


    void after(
            Class<?> clazz, String methodName, String methodDesc, Object target, Object returnObject, Map<String,Object> infos)
            throws Throwable;

    void error(Class<?> clazz, String methodName, String methodDesc, Object target, Map<String,Object> infos, Throwable throwable)
            throws Throwable;
}

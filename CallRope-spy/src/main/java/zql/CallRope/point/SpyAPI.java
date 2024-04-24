package zql.CallRope.point;

import java.util.Map;

public class SpyAPI {
    public static final SpySPI doNothingSpy = new DoNothingSpy();
    public static volatile SpySPI spyInstance = doNothingSpy;
    private static volatile boolean inited = false;


    public static void setSpy(SpySPI spy) {
        spyInstance = spy;
        inited = true;
    }

    public static boolean isInited() {
        return inited;
    }

    public static void setDoNothingSpy() {
        setSpy(doNothingSpy);
    }

    public static void destroySpySPI() {
        setDoNothingSpy();
        inited = false;
    }

    public static void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos) {
        spyInstance.atEnter(clazz, methodInfo, target, infos);
    }

    public static void atExit(Class<?> clazz, String methodInfo, Object target,
                              Object returnObject, Map<String, Object> infos) {
        spyInstance.atExit(clazz, methodInfo, target, returnObject, infos);
    }

    public static void atExceptionExit(Class<?> clazz, String methodInfo, Object target,
                                       Map<String, Object> infos, Throwable throwable) {
        spyInstance.atExceptionExit(clazz, methodInfo, target, infos, throwable);
    }

    public static void atFrameworkEnter(String traceId, String spanId, String parentSpanId, Map<String, Object> infos) {
        spyInstance.atFrameworkEnter(traceId, spanId, parentSpanId, infos);
    }

    public static void atFrameworkExit(String traceId, String spanId, String parentSpanId,Map<String, Object> infos) {
        spyInstance.atFrameworkExit(traceId, spanId, parentSpanId, infos);
    }

    private static class DoNothingSpy implements SpySPI {
        public void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String,Object> infos){}

        public void atExit(Class<?> clazz, String methodInfo, Object target, Object returnObject,Map<String,Object> infos) {}

        public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Map<String,Object> infos, Throwable throwable){}

        public void atFrameworkEnter(String traceId, String spanId, String parentSpanId, Map<String,Object> infos){}

        public void atFrameworkExit(String traceId, String spanId, String parentSpanId, Map<String,Object> infos){}
    }
}

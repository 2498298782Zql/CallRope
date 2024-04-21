package zql.CallRope.point;

public class SpyAPI {
    public static final SpySPI doNothingSpy = new DoNothingSpy();
    public static volatile SpySPI spyInstance = doNothingSpy;
    private static volatile boolean inited = false;


    public static void setSpy(SpySPI spy) {
        spyInstance = spy;
        inited = true;
    }

    public static boolean isInited(){
        return inited;
    }

    public static void setDoNothingSpy() {
        setSpy(doNothingSpy);
    }

    public static void destroySpySPI(){
        setDoNothingSpy();
        inited = false;
    }

    public static void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {
        spyInstance.atEnter(clazz, methodInfo, target, args);
    }

    public static void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args,
                              Object returnObject) {
        spyInstance.atExit(clazz, methodInfo, target, args, returnObject);
    }

    public static void atExceptionExit(Class<?> clazz, String methodInfo, Object target,
                                       Object[] args, Throwable throwable) {
        spyInstance.atExceptionExit(clazz, methodInfo, target, args, throwable);
    }

    public static void atFrameworkEnter(String traceId, String spanId, String parentSpanId) {
        spyInstance.atFrameworkEnter(traceId, spanId, parentSpanId);
    }

    private static class DoNothingSpy implements SpySPI {
        @Override
        public void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {

        }

        @Override
        public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {

        }

        @Override
        public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable) {

        }

        @Override
        public void atFrameworkEnter(String traceId, String spanId, String parentSpanId) {

        }

        @Override
        public void atFrameworkExit(String info) {

        }
    }
}

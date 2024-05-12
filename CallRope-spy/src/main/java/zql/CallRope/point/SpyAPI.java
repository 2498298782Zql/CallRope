package zql.CallRope.point;

import zql.CallRope.point.model.Span;

import java.util.Map;
import java.util.concurrent.Callable;

public class SpyAPI {
    public static final SpySPI doNothingSpy = new DoNothingSpy();
    private static volatile SpySPI spyInstance = doNothingSpy;
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

    public static void atFrameworkEnter(Span span, Map<String, Object> infos, String[] enhanceCLassnames) {
        spyInstance.atFrameworkEnter(span, infos,enhanceCLassnames);
    }

    public static void atFrameworkExit(Span span, Map<String, Object> infos, String[] enhanceCLassnames) {
        spyInstance.atFrameworkExit(span, infos, enhanceCLassnames);
    }

    public static void atFrameThreadPoolEnter(Span span,String[] enhanceCLassnames) {
        spyInstance.atFrameThreadPoolEnter(span,enhanceCLassnames);
    }

    public static void atFrameThreadPoolExit(Span span, String[] enhanceCLassnames) {
        spyInstance.atFrameThreadPoolExit(span, enhanceCLassnames);
    }

    private static class DoNothingSpy implements SpySPI {
        @Override
        public void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos) {
        }

        @Override
        public void atExit(Class<?> clazz, String methodInfo, Object target, Object returnObject, Map<String, Object> infos) {
        }

        @Override
        public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos, Throwable throwable) {
        }

        @Override
        public void atFrameworkEnter(Span span, Map<String, Object> infos, String[] enhanceCLassnames) {

        }

        @Override
        public void atFrameworkExit(Span span, Map<String, Object> infos, String[] enhanceCLassnames) {

        }

        @Override
        public void atFrameThreadPoolEnter(Span span, String[] enhanceCLassnames) {

        }

        @Override
        public void atFrameThreadPoolExit(Span span, String[] enhanceCLassnames) {

        }
    }
}

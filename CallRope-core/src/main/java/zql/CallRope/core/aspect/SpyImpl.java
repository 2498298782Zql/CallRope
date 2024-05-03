package zql.CallRope.core.aspect;

import zql.CallRope.core.aspect.aspectImpl.AsyncThreadImpl;
import zql.CallRope.core.aspect.aspectImpl.MethodAspectImpl;
import zql.CallRope.core.aspect.aspectImpl.SpringFrameworkAspectImpl;
import zql.CallRope.point.SpyAPI;
import zql.CallRope.point.SpySPI;
import zql.CallRope.point.model.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SpyImpl implements SpySPI {
    private static List<MethodAspect> methodAspects;
    private static List<FrameworkAspect> springFrameworkAspects;
    private static List<AsyncThreadAspect> asyncThreadAspects;


    public static void init() {
        methodAspects = new ArrayList<>();
        springFrameworkAspects = new ArrayList<>();
        asyncThreadAspects = new ArrayList<>();
        methodAspects.add(new MethodAspectImpl());
        springFrameworkAspects.add(new SpringFrameworkAspectImpl());
        asyncThreadAspects.add(new AsyncThreadImpl());
        SpyAPI.setSpy(new SpyImpl());
    }

    @Override
    public void atEnter(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.before(clazz, methodName, methodDesc, target, infos);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atExit(Class<?> clazz, String methodInfo, Object target, Object returnObject, Map<String, Object> infos) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.after(clazz, methodName, methodDesc, target, returnObject, infos);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Map<String, Object> infos, Throwable throwable) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.error(clazz, methodName, methodDesc, target, infos, throwable);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atFrameworkEnter(Span span, Map<String, Object> infos) {
        for (FrameworkAspect frameworkAspect : springFrameworkAspects) {
            try {
                frameworkAspect.entry(span, infos);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void atFrameworkExit(Span span, Map<String, Object> infos) {
        for (FrameworkAspect frameworkAspect : springFrameworkAspects) {
            try {
                frameworkAspect.exit(span, infos);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void atFrameThreadPoolEnter(Span span) {
        for (AsyncThreadAspect asyncThreadAspect : asyncThreadAspects) {
            try {
                asyncThreadAspect.enter(span, true, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void atFrameThreadPoolExit(Span span) {
        for (AsyncThreadAspect asyncThreadAspect : asyncThreadAspects) {
            try {
                asyncThreadAspect.exit(span, true, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private String[] splitMethodInfo(String methodInfo) {
        return methodInfo.split(Pattern.quote("|"));
    }
}

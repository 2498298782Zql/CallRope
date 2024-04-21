package zql.CallRope.core.aspect;

import zql.CallRope.core.aspect.aspectImpl.MethodAspectImpl;
import zql.CallRope.core.aspect.aspectImpl.SpringFrameworkAspectImpl;
import zql.CallRope.point.SpyAPI;
import zql.CallRope.point.SpySPI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SpyImpl implements SpySPI {
    private static List<MethodAspect> methodAspects;
    private static List<FrameworkAspect> springFrameworkAspects;

    public static void init() {
        methodAspects = new ArrayList<>();
        springFrameworkAspects = new ArrayList<>();
        methodAspects.add(new MethodAspectImpl());
        springFrameworkAspects.add(new SpringFrameworkAspectImpl());
        SpyAPI.setSpy(new SpyImpl());
    }

    @Override
    public void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.before(clazz, methodName, methodDesc, target, args);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.after(clazz, methodName, methodDesc, target, args, returnObject);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable) {
        try {
            String[] methodInfos = splitMethodInfo(methodInfo);
            String methodName = methodInfos[0];
            String methodDesc = methodInfos[1];
            for (MethodAspect methodAspect : methodAspects) {
                methodAspect.error(clazz, methodName, methodDesc, target, args, throwable);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atFrameworkEnter(String traceId, String spanId, String parentSpanId) {
        for (FrameworkAspect frameworkAspect : springFrameworkAspects) {
            try {
                frameworkAspect.entry(traceId, spanId, parentSpanId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void atFrameworkExit(String info) {
        for (FrameworkAspect frameworkAspect : springFrameworkAspects) {
            try {
                frameworkAspect.exit(info);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private String[] splitMethodInfo(String methodInfo) {
        return methodInfo.split(Pattern.quote("|"));
    }
}

package zql.CallRope.core.aspect.aspectSPI;

import zql.CallRope.core.aspect.AsyncThreadAspect;
import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.core.aspect.MethodAspect;

import java.util.Map;
import java.util.ServiceLoader;

public class aspectManager {
    private static Map<String, MethodAspect> methodAspects;

    private static Map<String, FrameworkAspect> frameworkAspects;

    private static Map<String, AsyncThreadAspect> asyncThreadAspects;

    public static void init(Map<String, MethodAspect> methodAspectsMap, Map<String, FrameworkAspect> frameworkAspectsMap, Map<String, AsyncThreadAspect> asyncThreadAspectsMap) {
        methodAspects = methodAspectsMap;
        frameworkAspects = frameworkAspectsMap;
        asyncThreadAspects = asyncThreadAspectsMap;
        loadAllAspects();
    }

    private static void loadMethodAspectsServices() {
        ServiceLoader<MethodAspect> loader = ServiceLoader.load(MethodAspect.class, MethodAspect.class.getClassLoader());
        for (MethodAspect impl : loader) {
            methodAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadFrameworkAspectsServices() {
        ServiceLoader<FrameworkAspect> loader = ServiceLoader.load(FrameworkAspect.class, FrameworkAspect.class.getClassLoader());
        for (FrameworkAspect impl : loader) {
            frameworkAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadAsyncThreadAspects() {
        ServiceLoader<AsyncThreadAspect> loader = ServiceLoader.load(AsyncThreadAspect.class, AsyncThreadAspect.class.getClassLoader());
        for (AsyncThreadAspect impl : loader) {
            asyncThreadAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadAllAspects() {
        loadAsyncThreadAspects();
        loadMethodAspectsServices();
        loadFrameworkAspectsServices();
    }
}

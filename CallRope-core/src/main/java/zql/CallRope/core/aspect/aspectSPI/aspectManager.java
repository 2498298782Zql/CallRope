package zql.CallRope.core.aspect.aspectSPI;

import zql.CallRope.core.aspect.AsyncThreadAspect;
import zql.CallRope.core.aspect.FrameworkAspect;
import zql.CallRope.core.aspect.MethodAspect;
import zql.CallRope.spi.SpiBs;
import zql.CallRope.spi.api.IExtensionLoader;

import java.util.List;
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
        IExtensionLoader<MethodAspect> load = SpiBs.load(MethodAspect.class, MethodAspect.class.getClassLoader());
        List<MethodAspect> allExtension = load.getAllExtension();
        for (MethodAspect impl : allExtension) {
            methodAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadFrameworkAspectsServices() {
        IExtensionLoader<FrameworkAspect> load = SpiBs.load(FrameworkAspect.class, FrameworkAspect.class.getClassLoader());
        List<FrameworkAspect> allExtension = load.getAllExtension();
        for (FrameworkAspect impl : allExtension) {
            frameworkAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadAsyncThreadAspects() {
        IExtensionLoader<AsyncThreadAspect> load = SpiBs.load(AsyncThreadAspect.class, AsyncThreadAspect.class.getClassLoader());
        List<AsyncThreadAspect> allExtension = load.getAllExtension();
        for (AsyncThreadAspect impl : allExtension) {
            asyncThreadAspects.put(impl.getClass().getSimpleName(), impl);
        }
    }

    private static void loadAllAspects() {
        loadAsyncThreadAspects();
        loadMethodAspectsServices();
        loadFrameworkAspectsServices();
    }
}

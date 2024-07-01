package zql.CallRope.spi;

import zql.CallRope.spi.api.IExtensionLoader;
import zql.CallRope.spi.api.impl.ExtensionLoader;
import zql.CallRope.spi.util.ArgsUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spi全局环境
 */
public final class SpiBs {

    private SpiBs(){}

    // final已经有防止指令重排的功能，无需volatile
    private static final Map<Class, ExtensionLoader> EX_LOADER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> IExtensionLoader<T> load(Class<T> clazz){
        ArgsUtil.notNull(clazz , "clazz");
        ExtensionLoader extensionLoader = EX_LOADER_MAP.get(clazz);
        if(extensionLoader != null){
            return extensionLoader;
        }
        // DLC
        synchronized (EX_LOADER_MAP){
            extensionLoader = EX_LOADER_MAP.get(clazz);
            if(extensionLoader == null){
                extensionLoader = new ExtensionLoader(clazz);
            }
        }
        return extensionLoader;
    }


    @SuppressWarnings("unchecked")
    public static <T> IExtensionLoader<T> load(Class<T> clazz, ClassLoader classLoader){
        ArgsUtil.notNull(clazz , "clazz");
        ExtensionLoader extensionLoader = EX_LOADER_MAP.get(clazz);
        if(extensionLoader != null){
            return extensionLoader;
        }
        // DLC
        synchronized (EX_LOADER_MAP){
            extensionLoader = EX_LOADER_MAP.get(clazz);
            if(extensionLoader == null){
                extensionLoader = new ExtensionLoader(clazz, classLoader);
            }
        }
        return extensionLoader;
    }

}

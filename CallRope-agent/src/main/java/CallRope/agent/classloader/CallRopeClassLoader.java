package CallRope.agent.classloader;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
public class CallRopeClassLoader extends URLClassLoader {
    public CallRopeClassLoader(URL[] urls) {
        super(urls,getSystemClassLoader().getParent());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedC = findLoadedClass(name);
        if (loadedC != null) {
            return loadedC;
        }
        // 优先从parent（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java.") || name.contains("zql.CallRope.point"))) {
            return super.loadClass(name, resolve);
        }

        try {
            Class<?> loadedClass = findClass(name);
            if (loadedClass != null) {
                if (resolve) {
                    resolveClass(loadedClass);
                }
                return loadedClass;
            }
        } catch (ClassNotFoundException ignored) {
            log.info("CallRopeClassLoader:ClassNotFoundException in CallRopeClassLoader");
            ignored.printStackTrace();
        }
        return super.loadClass(name, resolve);
    }
}

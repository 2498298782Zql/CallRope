package CallRope.agent.classloader;


import java.net.URL;
import java.net.URLClassLoader;

public class CallRopeClassLoader extends URLClassLoader {
    public CallRopeClassLoader(URL[] urls) {
        super(urls, getSystemClassLoader().getParent());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedC = findLoadedClass(name);
        if (loadedC != null) {
            return loadedC;
        }
        // 优先从（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java.") || name.contains("zql.CallRope.point")||name.startsWith("javax") || name.startsWith("org.slf4j"))) {
            return getSystemClassLoader().loadClass(name);
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
            ignored.printStackTrace();
        }
        return super.loadClass(name, resolve);
    }
}

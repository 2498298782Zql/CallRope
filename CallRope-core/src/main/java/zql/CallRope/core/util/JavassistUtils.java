package zql.CallRope.core.util;

import javassist.*;

import java.io.IOException;
import java.security.ProtectionDomain;

public class JavassistUtils {




    public static boolean isNative(CtBehavior method) {
        return Modifier.isNative(method.getModifiers());
    }

    public static boolean isAbstract(CtBehavior method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    public static boolean isStatic(CtBehavior method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public static boolean isClassAtPackageJavaUtil(String className) {
        return isClassAtPackage(className, "java.util");
    }

    public static boolean isClassAtPackage(String className, String packageName) {
        return packageName.equals(getPackageName(className));
    }

    public static String getPackageName(String className) {
        final int idx = className.lastIndexOf('.');
        if (-1 == idx) return "";
        return className.substring(0, idx);
    }

    public static boolean isClassUnderPackage(String className, String packageName) {
        String packageOfClass = getPackageName(className);
        return packageOfClass.equals(packageName) || packageOfClass.startsWith(packageName + ".");
    }

    public static CtClass getCtClass(ClassLoader loader, String className) throws IOException {
        try {
            ClassPool classPool = ClassPool.getDefault();
            if (loader == null) {
                classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
            } else {
                classPool.appendClassPath(new LoaderClassPath(loader));
            }
            CtClass ctClass = classPool.get(className);
            if (ctClass != null) {
                return ctClass;
            } else {
                return null;
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package zql.CallRope.core.util;

import javassist.CtBehavior;
import javassist.Modifier;

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
}

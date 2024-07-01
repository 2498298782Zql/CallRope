package zql.CallRope.spi.util;

public class ArgsUtil {
    public static void notNull(Object object, String name) {
        if (null == object) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }
}

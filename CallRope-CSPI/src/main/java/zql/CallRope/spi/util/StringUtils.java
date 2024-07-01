package zql.CallRope.spi.util;

public class StringUtils {
    public static boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }


    /**
     * 首字母小写 example: Apple -> apple
     * @param str
     * @return
     */
    public static String firstToLowerCase(String str) {
        if (str != null && str.trim().length() != 0) {
            return str.length() == 1 ? str.toLowerCase() : str.substring(0, 1).toLowerCase() + str.substring(1);
        } else {
            return str;
        }
    }
}

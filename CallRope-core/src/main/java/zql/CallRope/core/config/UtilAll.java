package zql.CallRope.core.config;

public class UtilAll {
    final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    public static String bytes2string(byte[] src) {
        char[] hexChars = new char[src.length * 2];

        for (int j = 0; j < src.length; j++) {
            int v = src[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


}

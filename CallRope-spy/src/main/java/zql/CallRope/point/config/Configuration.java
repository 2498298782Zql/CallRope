package zql.CallRope.point.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Configuration {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("CallRope-spy/src/main/resources/rope-spy.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Integer getPropertyAsInteger(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Long getPropertyAsLong(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Long.valueOf(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<String> getPropertyAsList(String key) {
        String values = properties.getProperty(key);
        if (values != null) {
            try {
                return splitByCommaToList(values);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static Set<String> getPropertyAsSet(String key) {
        String values = properties.getProperty(key);
        if (values != null) {
            try {
                return splitByCommaToSet(values);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 按英文逗号分割字符串
     */
    private static List<String> splitByCommaToList(String values) {
        List<String> result = new ArrayList<>();
        if (values != null && !values.isEmpty()) {
            String[] parts = values.split(",");
            for (String part : parts) {
                result.add(part.trim()); // 去除字符串两端的空格并添加到结果列表中
            }
        }
        return result;
    }

    /**
     * 按英文逗号分割字符串
     */
    private static Set<String> splitByCommaToSet(String values) {
        Set<String> result = new HashSet<>();
        if (values != null && !values.isEmpty()) {
            String[] parts = values.split(",");
            for (String part : parts) {
                result.add(part.trim()); // 去除字符串两端的空格并添加到结果列表中
            }
        }
        return result;
    }
}

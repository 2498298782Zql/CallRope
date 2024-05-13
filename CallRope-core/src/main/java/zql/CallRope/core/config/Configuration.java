package zql.CallRope.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Configuration {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = Configuration.class.getClassLoader().getResourceAsStream("rope-core.properties");
            // 使用properties对象加载输入流
            properties.load(in);
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

package zql.CallRope.point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


// 服务发现
public class SpyServiceManager {
    private static final List<SpySPI> services = new ArrayList<>();

    static {
        loadServices();
    }

    private static void loadServices() {
        ServiceLoader<SpySPI> loader = ServiceLoader.load(SpySPI.class);
        for (SpySPI service : loader) {
            register(service);
        }
    }

    public static void register(SpySPI service) {
        services.add(service);
    }

    public static void unregister(SpySPI service) {
        services.remove(service);
    }

    public static void addServiceToConfigFile(String className) {
        try (FileWriter fw = new FileWriter("META-INF/services/zql.CallRope.point.SpySPI", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

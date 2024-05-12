package zql.CallRope.demo.spi;

import zql.CallRope.point.SpySPI;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class test {
    private static final List<SayHi> services = new ArrayList<>();

    static {
        loadServices();
    }

    private static void loadServices() {
        ServiceLoader<SayHi> loader = ServiceLoader.load(SayHi.class);
        for (SayHi impl : loader) {
            register(impl);
        }
    }

    public static void register(SayHi impl) {
        services.add(impl);
    }

    public static void main(String[] args) {
        for(SayHi sayHi : services){
            sayHi.say();
        }
    }
}

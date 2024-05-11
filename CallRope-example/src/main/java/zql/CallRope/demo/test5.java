package zql.CallRope.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class test5 {
    public static void main(String[] args) {
        System.out.println(new AtomicInteger(0).getAndAdd(1));
        System.out.println(new AtomicInteger(0).addAndGet(1));
        User user = new User();
        user.age = 10;
        user.name = "zql";
        show(user);
    }

    private static <T> void show(Object object){
        System.out.println(object);
        System.out.println(object.getClass().getSimpleName());
    }
}

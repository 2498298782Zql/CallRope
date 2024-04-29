package zql.CallRope.point;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class example {
    public static TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程开启");
        threadLocal.set(1);

        executorService.submit(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.remove();
        });

        TimeUnit.SECONDS.sleep(1);
        threadLocal.set(2);
        executorService.submit(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.set(3);
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.remove();
        });

    }

}

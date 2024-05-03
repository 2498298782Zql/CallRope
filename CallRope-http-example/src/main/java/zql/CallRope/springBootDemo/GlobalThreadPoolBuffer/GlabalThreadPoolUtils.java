package zql.CallRope.springBootDemo.GlobalThreadPoolBuffer;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.*;

public class GlabalThreadPoolUtils {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
    public static TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程开启");
        threadLocal.set(1);

        executorService.submit(TtlRunnable.get(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
        }));

        TimeUnit.SECONDS.sleep(1);

        threadLocal.set(2);

        executorService.submit(TtlRunnable.get(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
        }));
    }



}

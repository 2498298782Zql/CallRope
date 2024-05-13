package zql.CallRope.springBootDemo.GlobalThreadPoolBuffer;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.*;

public class GlabalThreadPoolUtils {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new NamedThreadFactory("zql-pool",false));
    public static ThreadPoolExecutor shopPool = new ThreadPoolExecutor(5, 20, 1l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new NamedThreadFactory("shop-pool",false));
    public static TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        threadLocal.set(1);

        System.out.println(Thread.currentThread().getName());
        threadPoolExecutor.submit(TtlRunnable.get(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("子线程读取本地变量：" + threadLocal.get());
        }));

        TimeUnit.SECONDS.sleep(1);

        threadLocal.set(2);

        threadPoolExecutor.submit(TtlRunnable.get(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
        }));
    }



}

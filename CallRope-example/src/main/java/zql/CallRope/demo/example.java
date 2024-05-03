package zql.CallRope.demo;

import zql.CallRope.point.TransmittableThreadLocal;
import zql.CallRope.point.TtlRunnable;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class example {
    public static TransmittableThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);
    public static TransmittableThreadLocal<Span> hello = new TransmittableThreadLocal<>();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("主线程开启");
        threadLocal.set(1);
        hello.set(new SpanBuilder("0x11111","1.1","1","logincontroller", "test").build());
        executorService.submit(TtlRunnable.get(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.remove();
        }));

        TimeUnit.SECONDS.sleep(1);
        threadLocal.set(2);
        executorService.submit(TtlRunnable.get(() -> {
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.set(3);
            System.out.println("子线程读取本地变量：" + threadLocal.get());
            threadLocal.remove();
        }));

    }

}
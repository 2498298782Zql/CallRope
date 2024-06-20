package zql.CallRope.demo;

import zql.CallRope.point.threadpool.TransmittableThreadLocal;
import zql.CallRope.point.threadpool.TtlRunnable;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class example2 {
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
    public static TransmittableThreadLocal<Span> content = new TransmittableThreadLocal<>();
    public static void main(String[] args) {
        Span span = new SpanBuilder("1231313131", "0", "-1", "loginController", "login").build();
        content.set(span);
        threadPoolExecutor.submit(TtlRunnable.get(() -> {
            System.out.println("hello world");
        }));
    }
}
package zql.CallRope.point.IDutils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// 暂不启动
public class TokenBucket {
    private final int capacity; // 桶的容量
    private final AtomicInteger tokens; // 当前令牌数量
    private final int refillRate; // 每秒生成的令牌数
    private long lastRefillTimestamp; // 上次填充的时间戳（纳秒）
    private final Lock lock;


    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.tokens = new AtomicInteger(capacity);
        this.refillRate = refillRate;
        this.lastRefillTimestamp = System.nanoTime();
        this.lock = new ReentrantLock();
    }



    public boolean tryConsume() {
        refill();

        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }

        return false;
    }

    // 内部方法：根据时间间隔填充令牌
    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0;

        int newTokens = (int) (elapsedSeconds * refillRate);
        if (newTokens > 0) {
            lock.lock();
            try {
                tokens.set(Math.min(capacity, tokens.get() + newTokens));
                lastRefillTimestamp = now;
            } finally {
                lock.unlock();
            }
        }
    }
}

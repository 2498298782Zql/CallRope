package zql.CallRope.point;

import java.util.concurrent.Callable;

public interface Wrap {
    public Runnable doAutoWrap(Runnable runnable);

    public <T> Callable<T> doAutoWrap(Callable<T> callable);
}

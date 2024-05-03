package zql.CallRope.point;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class TransmittableThreadLocal<T> extends InheritableThreadLocal<T> {

    private static InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>> holder =
            new InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>>() {
                @Override
                protected WeakHashMap<TransmittableThreadLocal<Object>, ?> initialValue() {
                    return new WeakHashMap<TransmittableThreadLocal<Object>, Object>();
                }

                @Override
                protected WeakHashMap<TransmittableThreadLocal<Object>, ?> childValue(WeakHashMap<TransmittableThreadLocal<Object>, ?> parentValue) {
                    // 注意这里的WeakHashMap总是拷贝父线程的值
                    return new WeakHashMap<TransmittableThreadLocal<Object>, Object>(parentValue);
                }
            };

    // true:不允许存入空值，即相当于threadlocalmap的value不可以为null
    // false则允许存入空值
    private final boolean disableIgnoreNullValueSemantics;

    public TransmittableThreadLocal() {
        this(false);
    }

    public TransmittableThreadLocal(boolean disableIgnoreNullValueSemantics) {
        this.disableIgnoreNullValueSemantics = disableIgnoreNullValueSemantics;
    }

    public T copy(T parentValue) {
        return parentValue;
    }

    @Override
    public final T get() {
        T value = super.get();
        // 如果值不为NULL 或者 禁用了忽略空值的语义（也就是和ThreadLocal语义一致），则重新添加TTL实例自身到存储器
        if (disableIgnoreNullValueSemantics || null != value) addThisToHolder();
        return value;
    }

    public final void set(T value) {
        if (!disableIgnoreNullValueSemantics && value == null) {
            remove();
        } else {
            super.set(value);
            addThisToHolder();
        }
    }

    public void addThisToHolder() {
        if (!holder.get().containsKey(this)) {
            holder.get().put((TransmittableThreadLocal<Object>) this, null);
        }
    }

    @Override
    public final void remove() {
        removeThisFromHolder();
        super.remove();
    }

    private void superRemove() {
        super.remove();
    }

    public void removeThisFromHolder() {
        holder.get().remove(this);
    }


    private T copyValue() {
        return copy(get());
    }

    public static class Transmitter {
        public static class Snapshot {
            final WeakHashMap<TransmittableThreadLocal<Object>, Object> ttl2Value;

            private Snapshot(WeakHashMap<TransmittableThreadLocal<Object>, Object> ttl2Value) {
                this.ttl2Value = ttl2Value;
            }
        }

        public static Object capture() {
            return new Snapshot(captureTtlValues());
        }

        private static WeakHashMap<TransmittableThreadLocal<Object>, Object> captureTtlValues() {
            WeakHashMap<TransmittableThreadLocal<Object>, Object> ttl2Value = new WeakHashMap<TransmittableThreadLocal<Object>, Object>();
            for (TransmittableThreadLocal<Object> threadLocal : holder.get().keySet()) {
                ttl2Value.put(threadLocal, threadLocal.copyValue());
            }
            return ttl2Value;
        }

        public static Object replay(Object captured) {
            final Snapshot capturedSnapshot = (Snapshot) captured;
            return new Snapshot(replayTtlValues(capturedSnapshot.ttl2Value));
        }

        private static WeakHashMap<TransmittableThreadLocal<Object>, Object> replayTtlValues(WeakHashMap<TransmittableThreadLocal<Object>, Object> captured) {
            WeakHashMap<TransmittableThreadLocal<Object>, Object> backup = new WeakHashMap<TransmittableThreadLocal<Object>, Object>();
            for (final Iterator<TransmittableThreadLocal<Object>> iterator = holder.get().keySet().iterator(); iterator.hasNext(); ) {
                TransmittableThreadLocal<Object> threadLocal = iterator.next();
                backup.put(threadLocal, threadLocal.get());
                // 移除当前线程的threadlocalmap相比父线程Threadlocalmap中多的threadlocal实例
                if (!captured.containsKey(threadLocal)) {
                    iterator.remove();
                    threadLocal.superRemove();
                }
            }
            setParentTtlValuesToKidTtl(captured);
            return backup;
        }

        // 刷新holder中子线程对应map的所有threadlocal值,并且跟新holder对应的weakhashMap
        private static void setParentTtlValuesToKidTtl(WeakHashMap<TransmittableThreadLocal<Object>, Object> ttlValues) {
            for (Map.Entry<TransmittableThreadLocal<Object>, Object> entry : ttlValues.entrySet()) {
                TransmittableThreadLocal<Object> threadLocal = entry.getKey();
                // 这一步真正将父线程的threadlocal的值刷新到子线程中
                threadLocal.set(entry.getValue());
            }
        }

        public static void restore(Object backup) {
            final Snapshot backupSnapshot = (Snapshot) backup;
            restoreTtlValues(backupSnapshot.ttl2Value);
        }

        // 父线程交由子线程的任务执行完毕，恢复子线程的threadlocalmap
        private static void restoreTtlValues(WeakHashMap<TransmittableThreadLocal<Object>, Object> backup) {
            for(final Iterator<TransmittableThreadLocal<Object>> iterator= holder.get().keySet().iterator();iterator.hasNext();){
                TransmittableThreadLocal<Object> threadLocal = iterator.next();
                // 移除父线程的threadlocalmap相比当前线程(即子线程)Threadlocalmap中多的threadlocal实例
                if(!backup.containsKey(threadLocal)){
                    iterator.remove();
                    threadLocal.superRemove();
                }
            }
            setParentTtlValuesToKidTtl(backup);
        }

    }
}

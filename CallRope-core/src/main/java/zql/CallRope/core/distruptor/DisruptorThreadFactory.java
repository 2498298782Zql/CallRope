package zql.CallRope.core.distruptor;

import java.util.concurrent.ThreadFactory;

public class DisruptorThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final boolean  daemon;

    public DisruptorThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(daemon);
        t.setName(namePrefix + "-" + t.getId());
        return t;
    }

    public static ThreadFactory create(String namePrefix, boolean daemon){
        return new DisruptorThreadFactory(namePrefix, daemon);
    }

}

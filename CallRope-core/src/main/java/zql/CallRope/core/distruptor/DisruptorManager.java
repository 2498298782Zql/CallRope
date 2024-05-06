package zql.CallRope.core.distruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import zql.CallRope.core.distruptor.model.DataEvent;
import com.lmax.disruptor.*;

public class DisruptorManager<T> {
    private static final Integer DEFAULT_CONSUMER_SIZE = 4;
    public static final Integer DEFAULT_SIZE = 4096 << 1 << 1;
    private DataEventListener<T> dataEventListener;
    private DisruptorProducer<T> producer;
    private int ringBufferSize;
    private int consumerSize;

    public DisruptorManager(DataEventListener<T> dataEventListener) {
        this(dataEventListener, DEFAULT_CONSUMER_SIZE, DEFAULT_SIZE);
    }

    public DisruptorManager(DataEventListener<T> dataEventListener, int consumerSize, int ringBufferSize) {
        this.dataEventListener = dataEventListener;
        this.ringBufferSize = ringBufferSize;
        this.consumerSize = consumerSize;
    }

    public void start() {
        EventFactory<DataEvent<T>> eventFactory = new DisruptorEventFactory();
        Disruptor<DataEvent<T>> disruptor = new Disruptor<>(
                eventFactory,
                ringBufferSize,
                DisruptorThreadFactory.create("consumer-thread", false),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        DisruptorConsumer<T>[] consumers = new DisruptorConsumer[consumerSize];
        for (int i = 0; i < consumerSize; i++) {
            consumers[i] = new DisruptorConsumer<>(dataEventListener);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.start();
        RingBuffer<DataEvent<T>> ringBuffer = disruptor.getRingBuffer();
        this.producer = new DisruptorProducer<>(ringBuffer, disruptor);
    }

    public DisruptorProducer getProducer() {
        return this.producer;
    }

}

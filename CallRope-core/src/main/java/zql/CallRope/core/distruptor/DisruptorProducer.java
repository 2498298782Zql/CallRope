package zql.CallRope.core.distruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import zql.CallRope.core.distruptor.model.DataEvent;

public class DisruptorProducer<T> {
    private final RingBuffer<DataEvent<T>> ringBuffer;
    private final Disruptor<DataEvent<T>> disruptor;

    private final EventTranslatorOneArg<DataEvent<T>, T> translatorOneArg = (event, sequence, t) -> event.setData(t);

    public DisruptorProducer(RingBuffer<DataEvent<T>> ringBuffer, Disruptor<DataEvent<T>> disruptor) {
        this.ringBuffer = ringBuffer;
        this.disruptor = disruptor;
    }

    public void onData(final T data){
        try {
            ringBuffer.publishEvent(translatorOneArg, data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shutdown(){
        if(disruptor != null){
            disruptor.shutdown();
        }
    }

}

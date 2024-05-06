package zql.CallRope.core.distruptor;

import com.lmax.disruptor.WorkHandler;
import zql.CallRope.core.distruptor.model.DataEvent;

public class DisruptorConsumer<T> implements WorkHandler<DataEvent<T>> {
    private DataEventListener<T> dataEventListener;

    public DisruptorConsumer(DataEventListener<T> dataEventListener) {
        this.dataEventListener = dataEventListener;
    }

    @Override
    public void onEvent(DataEvent<T> dataEvent) throws Exception {
        if(dataEvent != null){
            dataEventListener.processDataEvent(dataEvent);
        }
    }
}

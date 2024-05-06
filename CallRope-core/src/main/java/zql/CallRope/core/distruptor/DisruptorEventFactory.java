package zql.CallRope.core.distruptor;

import com.lmax.disruptor.EventFactory;
import zql.CallRope.core.distruptor.model.DataEvent;

public class DisruptorEventFactory<T> implements EventFactory<DataEvent<T>> {
    @Override
    public DataEvent<T> newInstance() {
        return new DataEvent<T>();
    }
}

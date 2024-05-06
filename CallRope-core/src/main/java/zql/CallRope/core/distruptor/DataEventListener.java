package zql.CallRope.core.distruptor;

import zql.CallRope.core.distruptor.model.DataEvent;
import zql.CallRope.point.model.Span;

public interface DataEventListener<T> {
    public void processDataEvent(DataEvent<T> dataEvent);
}

package zql.CallRope.point.model;

import java.util.LinkedList;
import java.util.List;

public class Trace {
    private String traceId;
    private List<String> traceStackSegmentIds;

    private boolean finished;

    public Trace(String traceId) {
        this.traceId = traceId;
        this.traceStackSegmentIds = new LinkedList<>();
        finished = false;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<String> getTraceStackSegments() {
        return traceStackSegmentIds;
    }

    public boolean isFinished() {
        return finished;
    }

    public void finish() {
        this.finished = true;
    }
}

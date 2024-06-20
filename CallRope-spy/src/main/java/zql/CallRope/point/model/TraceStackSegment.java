package zql.CallRope.point.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 一个请求中的一个服务全程就是一个Segment
 * 涉及多个服务就是多个Segment
 */
public class TraceStackSegment {
    private String segmentId;
    private List<String> spanIdStacks;

    public TraceStackSegment(String segmentId) {
        this.segmentId = segmentId;
        spanIdStacks = new LinkedList<>();
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public List<String> getSpanStacks() {
        return spanIdStacks;
    }
}

package zql.CallRope.core.es;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlContentUtil {
    /**
     * Log转化为ES标准数据
     * 判断是否为Span类型
     */
    public static XContentBuilder getXContentBuilder(Object object) {
        if (object.getClass().getName().contains("Span")) {
            return getEsbMonitorXContentBuilder((Span) object);
        }
        return null;
    }

    /**
     * 将对象转换成 ES 标准数据格式
     */
    public static XContentBuilder getEsbMonitorXContentBuilder(Span span) {
        XContentBuilder xContentBuilder = null;
        try {
            xContentBuilder = XContentFactory.jsonBuilder().startObject()// 标识开始设置值
                    .field("trace_id", span.traceId)
                    .field("span_id", span.spanId)
                    .field("parent_span_id", span.pspanId)
                    .field("service_name", span.ServiceName)
                    .field("method_name", span.MethodName)
                    .field("enviroment", span.env)
                    .field("is_async_threadpool", span.isAsyncThread)
                    .field("duration", span.duration)
                    .field("start_time", span.start)
                    .field("end_time", span.end)
                    .field("log_infos", span.logInfos)
                    .endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xContentBuilder;
    }


    public static XContentBuilder getXContentBuilderMapping(Class clazz) throws IOException {
        if (clazz.getName().contains("Span")) {
            return XmlContentUtil.getEsbMonitorXContentBuilderMapping();
        }
        return null;
    }
    public static XContentBuilder getEsbMonitorXContentBuilderMapping() throws IOException {
        Map<String, Object> keyword = new HashMap<String, Object>();
        keyword.put("type", "keyword");
        keyword.put("ignore_above", 256);
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()//标识开始设置值
                .startObject("properties")
                .startObject("@timestamp").field("type","date").endObject()
                .startObject("@version").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("trace_id").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("span_id").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("parent_span_id").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("service_name").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("method_name").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("enviroment").field("type","text").startObject("fields").field("keyword",keyword).endObject().endObject()
                .startObject("is_async_threadpool").field("type", "boolean").endObject()
                .startObject("duration").field("type","long").endObject()
                .startObject("start_time").field("type", "date").endObject()
                .startObject("end_time").field("type", "date").endObject()
                .startObject("log_infos").field("type", "object").endObject()
                .endObject().endObject();
        return mapping;
    }


}

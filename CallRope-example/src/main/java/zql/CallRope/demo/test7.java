package zql.CallRope.demo;

import com.alibaba.fastjson.JSONObject;
import zql.CallRope.point.model.Span;
import zql.CallRope.point.model.SpanBuilder;

public class test7 {
    public static void main(String[] args) {
        Span build = new SpanBuilder("1", "2", "demp", "demo", "demo").build();
        String s = "{\"MethodName\":\"listUser\",\"ServiceName\":\"zql.CallRope.springBootDemo.controller.UserController\",\"duration\":250,\"end\":1715366105994,\"methodName\":\"listUs\n" +
                "er\",\"serviceName\":\"zql.CallRope.springBootDemo.controller.UserController\",\"start\":1715366105744,\"traceId\":\"50f8c0a8f085d17153661056880001\"}";
        Span span = JSONObject.parseObject(s, Span.class);
        System.out.println(span);
    }
}

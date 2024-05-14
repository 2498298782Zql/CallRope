# **无侵入式追踪链**

**目前只支持springboot，dubbo和跨线程池的链条追踪**

基于javaagent和javaassist框架实现，配合disruptor + rocketmq + elasticsearch 实现双缓冲 + 数据存储功能

其中项目中CallRope-http-example和CallRope-dubbo-producer-example为提供的dubbo追踪例子，可以看到没有任何的业务代码侵入

以下是使用说明:

**注： callRope-spy.jar为CallRope-spy项目的打包名字，同理callRope-core.jar是CallRope-core项目打包名字**

只需要用到CallRope-agent，CallRope-core，CallRope-spy三个项目，其他均为测试项目

只需配置项目的3个配置文件即可使用：

项目：CallRope-agent：rope-agent.properties

```
# 配置你存放 callRope-spy.jar 的服务器路径
rope_spy_jar=/root/CallRope/callRope-spy.jar
# 配置你存放 callRope-core.jar 的服务器路径
rope_core_jar=/root/CallRope/callRope-core.jar
# 这个不用修改，默认即可
rope_transform=zql.CallRope.core.instrumentation.CallRopeClassfileTransformer
```

项目：CallRope-core：rope-core.properties

```
# rocketmq的消费者组名字(以下简称mq)
mq_comsumer_group=span-consumer
# mq的主题名
mq_topic=callrope-span
# mq所在服务器ip
mq_host_name=192.168.240.133
# mq消费者数量
mq_comsumer_size = 8
# mq所在服务器端口
mq_port=9876

# es所在服务器ip
es_host_name=192.168.240.133
# es所在服务器端口
es_port=9200
# es索引名
es_index=rope-span

# disruptor无锁队列大小
disruptor_ring_buffer_size = 4096

#需要配置加强的dubbo项目的消费者过滤器，必须配置一个过滤器供追踪链加强，否则无法追踪
#，一个消费者只可配置一个被加强的过滤器，多个消费者可以配置多个，用逗号分隔
filter_comsumer_class=zql.CallRope.springBootDemo.filter.dubboFilter
#服务提供者方同理
filter_producer_class=com.filter.producerFilter

#springboot项目需要加强的controller层包前缀，即controller包相对地址
controller_package_prefix=zql.CallRope.springBootDemo.controller
# springboot项目需要加强的拦截器，用于请求初始traceID的赋值
enhance_interceptor_class=zql.CallRope.springBootDemo.handler.UserLoginInterceptor

# CallRope-spy项目打包后的jar包在服务器的位置
call_rope_spy_jar=/root/CallRope/callRope-spy.jar

```

项目：CallRope-spy.properties

```
#配置需要增强的项目线程池的线程名前缀
threadpool-name-prefix=zql-pool
```
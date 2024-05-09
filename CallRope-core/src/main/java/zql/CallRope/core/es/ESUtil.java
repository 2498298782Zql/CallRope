package zql.CallRope.core.es;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ESUtil {
    private static volatile TransportClient client;

    // 使用双端检索机制实现客户端为单例模式
    public static TransportClient getClient(String clusterName, String hostName, int hostPort) {
        if (client == null) {
            synchronized (TransportClient.class) {
                if (client == null) {
                    try {
                        client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", clusterName).build())
                                .addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), hostPort));
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return client;
    }

    /**
     * 判断索引是否存在
     */
    public static boolean existIndex(TransportClient client, String indexName) {
        boolean existIndex = false;
        try {
            existIndex = client.admin().indices().exists(new IndicesExistsRequest().indices(new String[]{indexName}))
                    .actionGet().isExists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existIndex;
    }

    /**
     * 创建并初始化索引
     *
     * @param clazz     需要创建索引的实体类
     * @param indexName 需要创建的索引名称
     */
    @SuppressWarnings("rawtypes")
    public static void initIndex(TransportClient client, String indexName, Class clazz) {
        try {
            if (existIndex(client, indexName)) {
                return; //如果该索引存在，则不创建
            }
            CreateIndexRequestBuilder cirBuilder = client.admin().indices().prepareCreate(indexName);
            XContentBuilder mapping = XmlContentUtil.getXContentBuilderMapping(clazz);
            cirBuilder.addMapping("doc", mapping);
            cirBuilder.execute().actionGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void addLogListIntoES(String indexName, List<T> logList, Class clazz) {
        TransportClient client = getClient();
        try {
            BulkRequestBuilder brBulider = client.prepareBulk();
            initIndex(client, indexName, clazz); //初始化索引
            IndexRequest request = null;
            for (Object log : logList) {
                request = client.prepareIndex(indexName, "doc")
                        .setSource(XmlContentUtil.getXContentBuilder(log)).request();
                brBulider.add(request);
            }
            BulkResponse bulkResponse = brBulider.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                throw new RuntimeException("日志写入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T> void addLogintoES(String indexName, T log, Class clazz) {
        TransportClient client = getClient();
        try {
            BulkRequestBuilder brBulider = client.prepareBulk();
            initIndex(client, indexName, clazz); //初始化索引
            IndexRequest request = null;
            request = client.prepareIndex(indexName, "doc")
                    .setSource(XmlContentUtil.getXContentBuilder(log)).request();
            brBulider.add(request);
            BulkResponse bulkResponse = brBulider.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                throw new RuntimeException("日志写入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static TransportClient getClient() {
        return client;
    }
}


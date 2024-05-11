package zql.CallRope.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import zql.CallRope.core.es.EsUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class test6 {

    public static void main(String[] args) throws IOException {
        AtomicInteger atomicInteger = new AtomicInteger(99);
        getClient("192.168.240.133", 9200);
        initIndex("user");
        User user = new User();
        user.name = "zql";
        user.age = 17;
        CreateResponse user1 = client.create(builder -> builder.index("user").id(atomicInteger.getAndAdd(1) + "").document(user));
        System.out.println(user1.result());
    }

    private static RestClient restClient;
    private static ElasticsearchTransport transport;
    private static volatile ElasticsearchClient client;

    public static ElasticsearchClient getClient(String es_hostName, Integer es_port) {
        if (client == null) {
            synchronized (EsUtil.class) {
                if (client == null) {
                    restClient = RestClient.builder(
                            new HttpHost(es_hostName, es_port)
                    ).build();
                    transport = new RestClientTransport(
                            restClient, new JacksonJsonpMapper()
                    );
                    client = new ElasticsearchClient(transport);
                }
            }
        }
        return client;
    }
    public static Boolean initIndex(String indexName) throws IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        if(client.indices().exists(req -> req.index(indexName)).value()){
            return true;
        }
        CreateIndexResponse callRope_span = client.indices().create(c -> c.index(indexName));
        Boolean status = callRope_span.acknowledged();
        return status;
    }
}

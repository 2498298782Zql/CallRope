package zql.CallRope.core.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import zql.CallRope.point.model.Span;

import java.io.IOException;
import java.util.List;

public class EsUtil {
    private static RestClient restClient;
    private static ElasticsearchTransport transport;
    private static volatile ElasticsearchClient client;

    public static ElasticsearchClient getClient() {
        return client;
    }

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
        if (client.indices().exists(req -> req.index(indexName)).value()) {
            return true;
        }
        CreateIndexResponse callRope_span = client.indices().create(c -> c.index(indexName));
        Boolean status = callRope_span.acknowledged();
        return status;
    }

    public static SearchResponse<Span> queryAllByIndex(String index) throws IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        SearchResponse<Span> response = client.search(builder -> builder.index(index), Span.class);
        List<Hit<Span>> hits = response.hits().hits();
        return response;
    }


    public static Boolean deleteIndex(String index) throws IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        // 删除索引
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(e -> e.index(index));
        return deleteIndexResponse.acknowledged();
    }

    public static <T> Result addDocument(String index, T object, String id) throws ObjectNotFoundException, IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        if (object == null) {
            throw new ObjectNotFoundException("对象为空");
        }
        CreateResponse createResponse = client.create(builder -> builder.index(index).id(id).document(object));
        return createResponse.result();
    }


    public static GetResponse<Object> queryDocument(String index, String id, Class clazz) throws IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        GetResponse<Object> getResponse = client.get(e -> e.index(index).id(id), clazz);
        return getResponse;
    }


    public static void clearClient() throws IOException {
        if (client == null) {
            throw new IllegalStateException("Elasticsearch client is not initialized.");
        }
        transport.close();
        restClient.close();
    }
}

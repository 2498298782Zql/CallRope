package zql.CallRope.core.es;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import com.google.gson.Gson;

import static zql.CallRope.core.es.ESUtil.existIndex;
import static zql.CallRope.core.es.ESUtil.getClient;


public class SearchUtil {
    private List<TermsAggregationBuilder> aggregationBuilders = null;
    private BoolQueryBuilder boolQueryBuilder  = QueryBuilders.boolQuery();
    public SearchUtil(){
        aggregationBuilders = new ArrayList<TermsAggregationBuilder>();
    }
    /**
     * 添加查询参数(查询类型为And)
     * @param key
     * @param value
     */
    public void setParamAnd(String key, String value){
        boolQueryBuilder.filter(QueryBuilders.termQuery(key, value));
    }
    /**
     * 添加模糊查询参数(查询类型为And)
     * @param key
     * @param value
     */
    public void setParamLike(String key, String value){
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery(key, "*"+value+"*"));
    }
    /**
     *
     *添加模糊查询参数(查询类型为And)(无分词查询)
     */
    public void setParamAndNotPhrase(String key, String value) {
        this.boolQueryBuilder.filter(QueryBuilders.termQuery(key + ".keyword", value));
    }
    /**
     * 添加查询参数(查询类型为Or)
     * @param key
     * @param value
     */
    public void setParamOr(String key, String value){
        boolQueryBuilder.should(QueryBuilders.termQuery(key,value));
    }

    /**
     * Date类型查询参数及范围设置
     * @param key
     * @param begin
     * @param end
     */
    public void setDateRange(String key, Date begin, Date end){
        String beginStr = DateUtil.getSub8DateStr(begin);
        String endStr = DateUtil.getSub8DateStr(end);
        setRange(key, beginStr, endStr);
    }

    /**
     *  Date类型查询参数及范围设置
     * @param key
     * @param begin
     * @param end
     */
    public void setRange(String key, String begin, String end){
        boolQueryBuilder.must(QueryBuilders.rangeQuery(key).from(begin).to(end));
    }
    /**
     * 添加聚合查询条件
     * @param key
     */
    public void setAggregationParam(String key){
        aggregationBuilders.add(AggregationBuilders.terms(key).field(key + ".keyword"));
    }
    /**
     * 获取ES集群下的所有索引名称
     * @return
     */
    public Set<String> getAllIndex() {
        TransportClient client = getClient();
        Set<String> indexSet = null;
        try {
            ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
            indexSet = isr.actionGet().getIndices().keySet();
        }catch(Exception e){
            e.printStackTrace();
        }
        return indexSet;
    }

    /**
     * 获取指定索引下的数据量
     * @param indexName
     * @return
     */
    public long getCountByIndex(String indexName) {
        TransportClient client = getClient();
        long totalHites = 0l;
        try {
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes("doc");
            searchRequestBuilder.setQuery(boolQueryBuilder);
            totalHites = searchRequestBuilder.get().getHits().getTotalHits();
        }catch(Exception e){
            e.printStackTrace();
        }
        return totalHites;
    }

    /**
     * 聚合检索
     * @param indexName
     * @return
     */
    public Map<String, Aggregation> getResultByAggreation(String indexName) {
        TransportClient client = getClient();
        try {
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes("doc");
            //添加查询条件
            searchRequestBuilder.setQuery(boolQueryBuilder);
            for (TermsAggregationBuilder termsAggregationBuilder : aggregationBuilders) {
                searchRequestBuilder.addAggregation(termsAggregationBuilder);
            }
            SearchResponse actionGet = searchRequestBuilder.execute().actionGet();
            Aggregations aggregations = actionGet.getAggregations();
            Map<String, Aggregation> asMap = aggregations.getAsMap();
            return asMap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据日志traceID查询单条记录
     * @param indexName
     * @return
     */
    public Object getByUuid(String indexName, Class clazz) {
        TransportClient client = getClient();
        Object logObj = null;
        try {
            if(!existIndex(client,indexName)){
                return null;
            }
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
                    .setSearchType(SearchType.DEFAULT)
                    .setSize(10)
                    .setScroll(new TimeValue(1000));
            //添加查询条件
            searchRequestBuilder.setQuery(boolQueryBuilder);
            SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
            SearchHits hitsFirst = searchResponse.getHits();
            Gson gson = new Gson();
            for (SearchHit hit : hitsFirst) {
                logObj = gson.fromJson(hit.getSourceAsString(), clazz);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logObj;
    }
}

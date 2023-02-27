package elasticsearchsearch.utils;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author : 其然乐衣Letitbe
 * @date : 2023/2/26
 */
@Service("LetitbeRestClient")
public class RestClient {

    @Autowired
    private RestHighLevelClient client;


    public SearchHit[] searchBlogList(String indexName, Set<String> stringSet, String[] filenames, Map<String, String> sort, Integer startIndex, Integer size) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest(indexName);

            // 2.准备DSL
            // 2.1 query
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            request.source().query(QueryBuilders.matchAllQuery());


            // 2.3 选择性过滤查询
            if ( !stringSet.isEmpty() && stringSet != null && filenames != null ) {
                for ( String value : stringSet ) {
                    for ( String target : filenames ) {
                        boolQuery.should(QueryBuilders.matchQuery(target, value));
                    }
                }
                boolQuery.minimumShouldMatch(1);
            }

            request.source().query(boolQuery);

            // 2.4 排序
            if (sort.get("way").equals("DESC")) {
                // 降序
                request.source().sort(sort.get("condition"), SortOrder.DESC);
            } else {
                // 升序
                request.source().sort(sort.get("condition"), SortOrder.ASC);
            }

            // 2.5 分页查询
            request.source().from(startIndex).size(size);

            // 3. 发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = handleResponce(response);

            return hits;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // 解析响应
    public SearchHit[] handleResponce(SearchResponse response) {
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        long total = searchHits.getTotalHits().value;
        // 4.2.文档数据
        SearchHit[] hits = searchHits.getHits();
        return hits;
    }
}

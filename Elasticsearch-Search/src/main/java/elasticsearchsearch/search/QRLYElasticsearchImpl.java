package elasticsearchsearch.search;

import com.alibaba.fastjson.JSON;
import elasticsearchsearch.Constants.EsConstants;
import elasticsearchsearch.utils.Jieba;
import elasticsearchsearch.utils.RestClient;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author : 其然乐衣Letitbe
 * @date : 2023/2/26
 */
@Service
public class QRLYElasticsearchImpl implements QRLYElasticsearch {

    @Autowired
    private Jieba jieba;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    @Override
    public void buildIndex(String indexName, String[] filenames) {

        String DSL = EsConstants.ES_INDEX_PRE;
        for ( String filename : filenames ) {
            DSL +=  "      \"" + filename + EsConstants.ES_INDEX_MID;
        }
        DSL += EsConstants.ES_INDEX_END;

        try {
            //1.创建Request对象
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            //2.准备请求的参数：DSL语句
            request.source(DSL, XContentType.JSON);
            //3.发送请求
            client.indices().create(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public void deleteIndex(String indexName) throws IOException {
        try {
            //1.创建Request对象
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            //2.发送请求
            client.indices().delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> getPhraseOfKeyword(String indexName, String keyword) {
        try {
            List<String> suggestionList = new ArrayList<>();
            Set<String> keywords = jieba.JIEBA(keyword);

            // 1. 准备Requst
            SearchRequest request = new SearchRequest(indexName);
            // 2. 准备DSL
            for ( String kw : keywords ) {
                request.source().suggest(new SuggestBuilder().addSuggestion(
                        "suggestion",
                        SuggestBuilders.completionSuggestion("suggestion")
                                .prefix(kw)
                                .size(10)
                ));
                // 3. 发起请求
                SearchResponse response = client.search(request, RequestOptions.DEFAULT);
                // 4. 解析结果
                Suggest suggest = response.getSuggest();
                // 4.1. 根据补全查询名称， 获取补全结果
                CompletionSuggestion suggestions = suggest.getSuggestion("suggestion");
                // 4.2. 获取options
                List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
                // 4.3. 遍历
                for (CompletionSuggestion.Entry.Option option : options) {
                    if ( option != null ) {
                        suggestionList.add( option.getText().toString() );
                    }
                    System.out.println( option.getText().toString() );
                }
            }
            return suggestionList;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }



    @Override
    public List<Object> getResultListByKeyword_Page_Sort(
                                      String indexName,
                                      String keyword, String[] filenames,
                                      Integer startIndex, Integer size,
                                      String sortCondition, String sortWay) {

        Set<String> keywordList = jieba.JIEBA( keyword );

        Map<String, String> sort = new HashMap<>();
        sort.put("condition", sortCondition);
        sort.put("way", sortWay);

        SearchHit[] hits = restClient.searchBlogList(indexName, keywordList, filenames,sort, startIndex, size);
        System.out.println("-----------------------------------------------");
        // 反序列化获取博文列表
        List<Object> objects = hitsToBlogList(hits);
        return objects;
    }

    // 反序列化
    public List<Object> hitsToBlogList( SearchHit[] hits ) {
        List<Object> blogList = new ArrayList<>();
        // 反序列
        for ( SearchHit hit : hits ) {
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Object object = JSON.parseObject(json, Object.class);
            blogList.add(object);
        }
        return blogList;
    }
}

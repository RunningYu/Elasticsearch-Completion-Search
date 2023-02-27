package elasticsearchsearch;

import com.alibaba.fastjson.JSON;
import elasticsearchsearch.enttys.Man;
import elasticsearchsearch.search.QRLYElasticsearch;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElasticsearchSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
//
//        String src = ElasticsearchSearchApplication.class.getClassLoader().getResource("application.yml").getPath();
//        System.out.println( "src : " + src );
//        Yaml yaml = new Yaml();
//        FileWriter fileWriter;
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(src));
//            Map<String, Object> yamlMap = yaml.load(fileInputStream);
//            Map<String, Object> esMap = (Map<String, Object>) yamlMap.get("Elasticsearch");
//            System.out.println(esMap);
//            // 修改配置
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    @Test
    void test1() throws IOException {

        List<Man> manList = new ArrayList<>();
        Man man0 = new Man("1", "路飞加强版", "重庆理工大学/花溪校区");
        manList.add( man0 );
        Man man1 = new Man("2", "路费", "重庆邮电大学");
        manList.add( man1 );
        Man man2 = new Man("3", "大作", "重庆理工大学/两江校区");
        manList.add( man2 );
        Man man3 = new Man("4", "大左" , "重庆师范大学");
        manList.add( man3 );
        Man man4 = new Man("5", "鲁非", "重庆大学");
        manList.add( man4);


        BulkRequest request = new BulkRequest();
        for ( Man man : manList ) {
            request.add(new IndexRequest("man_suggestion")
                    .id(man.getId())
                    .source(JSON.toJSONString(man), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);

    }

    @Test
    void test2() throws IOException {

        // 1. 准备Requst
        SearchRequest request = new SearchRequest("test");
        // 2. 准备DSL
        request.source().suggest(new SuggestBuilder().addSuggestion(
                "suggestion",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix("ch")
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
            System.out.println( option.getText().toString() );
        }
    }

    @Autowired
    private QRLYElasticsearch qrlyElasticsearch;

    @Test
    void search() {
//        String[] phrase = qrlyElasticsearch.getPhraseOfKeyword("test", "ch");
//        System.out.println( phrase );
        List<Object> list = qrlyElasticsearch.getResultListByKeyword_Page_Sort( "test", "重庆", new String[]{"address"}, 0, 10, "id", "DESC");
        System.out.println( list );
    }


    @Test
    void buildEsIndex() throws IOException {
        String[] filenames = new String[]{"name", "address"};
//        qrlyElasticsearch.buildIndex("man_suggestion1", filenames );
//
//        qrlyElasticsearch.deleteIndex("man_suggestion1");

        List<String> phrase = qrlyElasticsearch.getPhraseOfKeyword("man_suggestion", "重庆 大左");
        System.out.println( phrase );
    }

}

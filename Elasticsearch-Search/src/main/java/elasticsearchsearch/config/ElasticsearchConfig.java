package elasticsearchsearch.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * ES搜索引擎的配置
 * @author : 其然乐衣Letitbe
 * @date : 2022/12/11
 */
@Configuration("LetitbeElasticsearchConfig")
//@RefreshScope
public class ElasticsearchConfig {

    @Value("${es.url}")
    private String url;

    @Bean()
//    @Primary
    public RestHighLevelClient client(){
        System.out.println( url );
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create(url)
        ));
    }
}

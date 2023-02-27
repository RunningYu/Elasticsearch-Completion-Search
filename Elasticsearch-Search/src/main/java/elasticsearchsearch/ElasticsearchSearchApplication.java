package elasticsearchsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("elasticsearchsearch.**")
public class ElasticsearchSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchSearchApplication.class, args);
    }

}

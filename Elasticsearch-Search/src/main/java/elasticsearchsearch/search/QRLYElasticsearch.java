package elasticsearchsearch.search;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author : 其然乐衣Letitbe
 * @date : 2023/2/26
 */
public interface QRLYElasticsearch {

    void buildIndex(String indexName, String[] filename);

    void deleteIndex(String indexName) throws IOException;

    List<String> getPhraseOfKeyword(String indexName, String keyword);

    List<Object> getResultListByKeyword_Page_Sort(
                               String indexName,
                               String keyword, String[] filenames,
                               Integer startIndex, Integer size,
                               String sortCondition, String sortWay);

}

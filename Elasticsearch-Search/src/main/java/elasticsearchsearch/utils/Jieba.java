package elasticsearchsearch.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * jieba结巴分词 工具类
 * @author : 其然乐衣Letitbe
 * @date : 2022/12/15
 */
@Component("LetitbeJieba")
public class Jieba {
    public Set<String> JIEBA(String text){
        JiebaSegmenter jieba = new JiebaSegmenter();
        List<String> list = jieba.sentenceProcess(text);
        Set<String> wordList = new HashSet<>();
		wordList.add(text);
        for ( String word : list ) {
            wordList.add( word );
        }
        return wordList;
    }
}

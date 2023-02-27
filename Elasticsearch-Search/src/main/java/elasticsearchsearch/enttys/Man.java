package elasticsearchsearch.enttys;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : 其然乐衣Letitbe
 * @date : 2023/2/26
 */
@Data
public class Man {
    private String id;
    private String name;
    private String address;
    private List<String> suggestion;

    public Man(String id, String name, String address ) {
        this.id = id;
        this.name = name;
        this.address = address;
        // 将name，address 作为不全的内容放进suggestion
        if ( this.address.contains("/") ) {
            String[] arr = this.address.split("/");
            this.suggestion = new ArrayList<>();
            this.suggestion.add(this.name);
            Collections.addAll(this.suggestion, arr);
        }
        suggestion = Arrays.asList(this.name, this.address);
    }

    public Man() {
        // 将name，address 作为不全的内容放进suggestion
        if ( this.suggestion.contains("/") ) {
            String[] arr = this.address.split("/");
            this.suggestion = new ArrayList<>();
            this.suggestion.add(this.name);
            Collections.addAll(this.suggestion, arr);
        }
        suggestion = Arrays.asList(this.name, this.address);
    }

}

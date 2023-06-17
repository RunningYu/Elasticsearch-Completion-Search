

# Elasticsearch-Completion-Search

# <font color="red">**使用该组件的前提需要做得东西**：</font>

- 自己先在云服务器上（推荐）或是本地安装好<mark>**Elasticsearch**</mark>，然后把ip和端口信息在自己的yml配种文件中的es: url填上.
- 在云服务器上下载好<mark>中文分词器</mark>和<mark>pinyin分词器</mark>组件，整合好.
- 在Spring Boot项目中的Application启动类上加上 <mark>@ComponentScan("elasticsearchsearch.**")</mark>注解

# 本搜索自动补全功能组件 简述：

​	 用Elasticsearch来实现自动补全功能，在搜索框输入关键字，可以获取到自动补全的 suggestion词组

# 使用说明：

QRLYElasticsearch 中有四个接口：buildIndex、deleteIndex、getPhraseOfKeyword、getResultListByKeyword_Page_Sort

- ```
  void buildIndex(String indexName, String[] filename);
  ```

  - 该接口主要是负责在你配置安装的Elasticsearch创建一个ES索引库（用于搜索自动补全的suggestion词组的获取）
  - 参数说明：
    - String indexName :  创建的ES索引库的库名（注意：要全小写字母）
    - String[] filename : 自动补全的目标字段数组。即搜索时你需要自动补全的依据内容，比如你需要根据地方名和宾馆名来获取自动补全词组，那么就传你实体类中对应的字段名（如：placesName、hotelName），这个数组可以来自不同的实体类的汇总。

- ```
  void deleteIndex(String indexName) throws IOException;
  ```

  - 该接口是用于根据indexName索引库名来删除ES中的索引库

- ```
  List<String> getPhraseOfKeyword(String indexName, String keyword);
  ```

  - 在搜索框输入keyword关键字，便可以获取到自动补全的词组
  - 参数说明：
    - String indexName : 对应自动补全的索引库名（由buildIndex接口实现创建的），里面是包含了自动补全的目标内容
    - String keyword : 输入的关键字

- ```
  List<Object> getResultListByKeyword_Page_Sort(
                             String indexName,   // ES索引库的库名
                             String keyword, 	   // 输入的关键词
                             String[] filenames, // indexName索引库中的字段名
                             Integer startIndex, Integer size,  // 分页查询的其实索引，搜索一页的大小
                             String sortCondition,  // 排序的依据字段名（如果没有分页查询的需求，则传null即可）
                             String sortWay);		  // 排序规则：DESC | ASC，如果没有分页查询的需求，则传null即可
  ```

  - 该接口适合一般都会使用到的 关键字搜索 分页查询。
  - 通过输入关键字 keyword，后台会进行分词（如：java后端开发，就会分成“java” 、"后端"、“开发”、“java后端开发”），然后根据分词词组的内容去进行模糊查询，这样可以搜索到更加全面的内容
  - filenames ：是字段名数组，就是需要根据indexName索引库中的那些字段名来进行模糊查询的
  - ps : 参数中，如果没有升降序需求，传null即可


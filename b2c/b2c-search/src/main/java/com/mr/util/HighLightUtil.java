package com.mr.util;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class HighLightUtil {

    /**
     * 高亮工具类
     * @param elasticsearchTemplate
     * @param
     * @param poType
     * @param highField
     * @return
     */
    public  static  Map<Long,String> getHignLigntMap(ElasticsearchTemplate elasticsearchTemplate, SearchQuery searchQuery, Class poType, String highField){

        //增加高亮设置

        AggregatedPage<Map> ai=  elasticsearchTemplate.queryForPage(searchQuery,poType,new SearchResultMapper(){


            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                //定义查询出来内容存储的集合
                List<Map<String,Object>> content = new ArrayList<>();
                //获取高亮的结果
                SearchHits searchHits = searchResponse.getHits();
//                System.out.println(searchHits);

                if(searchHits!=null) {
                    //获取高亮中所有的内容
                    SearchHit[] hits = searchHits.getHits();
                    if(hits.length > 0) {
                        for (SearchHit hit : hits) {
                            Map<String,Object> map=new HashMap<>();
                            //高亮结果的id值
                            String id = hit.getId();
                            //存入Map
                            map.put("id",id);
                            //获取第一个字段的高亮内容
                            HighlightField highlightField1 = hit.getHighlightFields().get(highField);

                            if(highlightField1 != null) {
                                //获取第一个字段的值并封装map
                                String hight_value1 = highlightField1.getFragments()[0].toString();
                                map.put("value",hight_value1);
//                                System.out.println(hight_value1);
                            }else {
                                //获取原始的值
                                String value = (String) hit.getSourceAsMap().get(highField);
                                map.put("value",value);
                            }

                            content.add(map);
                        }
                    }
                }
                AggregatedPage aggregatedPage=new AggregatedPageImpl(content);
                return aggregatedPage;

            }

        });

        //数据由List转为map
        Map<Long,String> resultMap=new HashMap();
        ai.forEach(map->{
//            System.out.println(map);
            System.err.println(map.get("id")+"  "+map.get("value"));
            resultMap.put(Long.valueOf( map.get("id").toString()),map.get("value").toString());
        });
        return resultMap;
    }
}
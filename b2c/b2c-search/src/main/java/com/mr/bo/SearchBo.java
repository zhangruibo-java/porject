package com.mr.bo;

import lombok.Data;

import java.util.Map;

@Data
public class SearchBo {
    private String key;
    private Integer size= 10;
    private Integer page= 0;
    //{key: "华为", filter: {CPU核数: "八核", CPU品牌: "海思（Hisilicon）"}, page: 0}
    private Map<String,String> filter;
}

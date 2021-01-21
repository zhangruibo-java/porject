package com.mr.bo;

import lombok.Data;

@Data
public class SearchBo {
    private String key;
    private Integer size= 10;
    private Integer page= 0;
}

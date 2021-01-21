package com.mr.dao;

import com.mr.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodRepository extends ElasticsearchRepository<Goods,Long> {
}

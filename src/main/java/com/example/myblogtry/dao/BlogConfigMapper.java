package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogConfig;

import java.util.List;
import java.util.Map;

public interface BlogConfigMapper {

    List<BlogConfig> findConfigAll();

    int updateConfigByPrimaryKey(BlogConfig blogConfig);
}

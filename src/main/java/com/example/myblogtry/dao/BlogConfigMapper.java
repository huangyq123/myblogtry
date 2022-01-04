package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogConfig;

import java.util.List;

public interface BlogConfigMapper {

    List<BlogConfig> findConfigAll();
}

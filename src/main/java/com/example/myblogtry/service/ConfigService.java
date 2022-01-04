package com.example.myblogtry.service;

import com.example.myblogtry.entity.BlogConfig;

import java.util.List;
import java.util.Map;


public interface ConfigService {


    Map<String, String> getConfigList();

    int updateConfig(String configName,String configValue);

}

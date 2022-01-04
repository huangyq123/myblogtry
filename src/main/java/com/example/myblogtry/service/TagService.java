package com.example.myblogtry.service;

import com.example.myblogtry.entity.BlogTag;
import com.example.myblogtry.entity.BlogTagCount;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;

import java.util.List;

public interface TagService {

    PageResult getTagPage(PageQueryUtil pageQueryUtil);

    PageResult getTagPage(String page, String limit);

    boolean saveTag(String tagName);

    boolean deleteTags(Integer[] ids);

    List<BlogTagCount> getBlogTagCountForIndex();
}

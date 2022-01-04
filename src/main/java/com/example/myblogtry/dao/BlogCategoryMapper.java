package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.utils.PageQueryUtil;

import java.util.List;

public interface BlogCategoryMapper {

    List<BlogCategory> findCategoryList(PageQueryUtil pageUtil);

    int insertSelective(BlogCategory blogCategory);

    BlogCategory selectByCategoryName(String categoryName);

    int deleteSelective(Integer[] ids);

    BlogCategory selectByCategoryId(Integer categoryId);

    int updateByPrimaryKeySelective(BlogCategory blogCategory);

    List<BlogCategory> selectAll();


    BlogCategory selectByPrimaryKey(Integer blogCategoryId);
}

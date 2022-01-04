package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogTag;
import com.example.myblogtry.entity.BlogTagCount;
import com.example.myblogtry.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogTagMapper {

    List<BlogTag> findTagListByUtil(PageQueryUtil pageQueryUtil);

    List<BlogTag> findTagList(@Param("start") Integer start,@Param("limit") Integer limit);

    int save(BlogTag blogTag);

    BlogTag selectByName(String tagName);

    int deleteTagsByPrimaryKeys(Integer[] ids);

    List<BlogTagCount> getTagCount();
}

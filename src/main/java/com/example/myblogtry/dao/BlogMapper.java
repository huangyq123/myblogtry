package com.example.myblogtry.dao;

import com.example.myblogtry.controller.vo.BlogDetailVO;
import com.example.myblogtry.entity.Blog;
import com.example.myblogtry.utils.PageQueryUtil;

import java.util.List;

public interface BlogMapper {

    List<Blog> findBlogList(PageQueryUtil pageQueryUtil);


    Blog selectByPrimaryKey(Long id);

    int insertSelective(Blog blog);

    int updateByPrimaryKeySelective(Blog blog);

    int deleteByPrimaryKeys(Integer[] ids);

    List<Blog> findNewestBlog(PageQueryUtil pageQueryUtil);

    List<Blog> findMostViewsBlog(PageQueryUtil pageQueryUtil);

    //自己的实现方式
    BlogDetailVO findBlogDetailVOById(Long id);

    int updateByPrimaryKey(Blog blog);

    List<Blog> selectBlogByTagName(PageQueryUtil pageQueryUtil);

    List<Blog> selectBlogByCategoryName(PageQueryUtil pageQueryUtil);

    Blog selectBySubUrl(String subUrl);

}

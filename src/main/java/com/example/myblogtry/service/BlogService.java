package com.example.myblogtry.service;

import com.example.myblogtry.controller.vo.BlogDetailVO;
import com.example.myblogtry.entity.Blog;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;

import java.util.List;

public interface BlogService {

    PageResult getBlogPage(PageQueryUtil pageQueryUtil);

    Blog getBlogById(Long id);

    Boolean saveBlog(Blog blog);

    Boolean updateBlog(Blog blog);

    Boolean deleteBlog(Integer[] ids);

    List<Blog> getBlogSortByTime(PageQueryUtil pageQueryUtil);

    List<Blog> getBlogSortByViews(PageQueryUtil pageQueryUtil);

    //自己实现
    BlogDetailVO getBlogdetailVOById(Long id);
    //项目实现
    BlogDetailVO getBlogDetail(Long blogId);

    BlogDetailVO  getBlogDetailVO(Blog blog);

    PageResult getBlogPageByTagName(int page,String tagName);

    PageResult getBlogPageByCategoryName(int page,String categoryName);

    BlogDetailVO getBlogDetailBySubUrl(String subUrl);
}

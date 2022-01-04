package com.example.myblogtry.service;

import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    PageResult getBlogCategoryPage(PageQueryUtil pageQueryUtil);

    Boolean saveCategory(String categoryName,String categoryIcon);

    Boolean deleteCategory(Integer[] ids);

    Boolean updateCategory(String categoryId,String categoryName,String categoryIcon);

    List<BlogCategory> getAllCategories();
}

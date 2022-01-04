package com.example.myblogtry.service.Impel;

import com.example.myblogtry.dao.BlogCategoryMapper;
import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.service.CategoryService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import com.example.myblogtry.utils.ResultGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public PageResult getBlogCategoryPage(PageQueryUtil pageQueryUtil) {
        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryList(pageQueryUtil);
        //调用mapper的方法来进行查询结果
        PageResult pageResult = new PageResult(categoryList, categoryList.size(), pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        //封装成页面需要的返回的类型
        return pageResult;
    }

    @Override
    public Boolean saveCategory(String categoryName,String categoryIcon) {
        //考虑重复性，进行验证
        BlogCategory blogCategory1 = blogCategoryMapper.selectByCategoryName(categoryName);
        if(null==blogCategory1) {
            //未重复
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryRank(0);
//        blogCategory.setIsDeleted();
            blogCategory.setCreateTime(new Date());
            if (blogCategoryMapper.insertSelective(blogCategory) > 0) {
                return true;
            } else {
                //操作失败
                return false;
            }
        }else{
            //重复
            return false;
        }
    }

    //事务完整性——多条操作
    @Override
//    @Transactional
    public Boolean deleteCategory(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        int i = blogCategoryMapper.deleteSelective(ids);
        if(i==ids.length) {
            return true;
        }else{
            return false;
        }
    }

    @Override
//    @Transactional
    public Boolean updateCategory(String categoryId, String categoryName, String categoryIcon) {
        int newCategoryId = Integer.parseInt(categoryId);
        BlogCategory blogCategory = blogCategoryMapper.selectByCategoryId(newCategoryId);
        if(blogCategory!=null) {
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            if(blogCategoryMapper.updateByPrimaryKeySelective(blogCategory)>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BlogCategory> getAllCategories() {
        List<BlogCategory> blogCategories = blogCategoryMapper.selectAll();
        return blogCategories;
    }


}

package com.example.myblogtry.service.Impel;

import com.example.myblogtry.controller.vo.BlogDetailVO;
import com.example.myblogtry.dao.BlogCategoryMapper;
import com.example.myblogtry.dao.BlogCommentMapper;
import com.example.myblogtry.dao.BlogMapper;
import com.example.myblogtry.entity.Blog;
import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.service.BlogService;
import com.example.myblogtry.utils.MarkDownUtil;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private BlogCategoryMapper blogCategoryMapper;
    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Override
    public PageResult getBlogPage(PageQueryUtil pageQueryUtil) {
        List<Blog> blogList = blogMapper.findBlogList(pageQueryUtil);
        PageResult pageResult = new PageResult(blogList, blogList.size(), pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Blog getBlogById(Long id) {
//        int newId = Integer.parseInt(id);
        if(id<1){
            return null;
        }
        Blog blog = blogMapper.selectByPrimaryKey(id);
        return blog;
    }

    @Override
    @Transactional
    public Boolean saveBlog(Blog blog) {
        //考虑这个属性没有默认值，且不能为null
        if(null==blog.getBlogCategoryName()){
            blog.setBlogCategoryName("默认分类");
            if(0!=blog.getBlogCategoryId()){
                BlogCategory blogCategory = blogCategoryMapper.selectByCategoryId(blog.getBlogCategoryId());
                if(null!=blogCategory){
                   blog.setBlogCategoryName(blogCategory.getCategoryName());
                }else{
                    blog.setBlogCategoryName("默认分类");
                    blog.setBlogCategoryId(0);
                }
            }
        }
        if(blogMapper.insertSelective(blog)>0){
            //考虑博客保存对其他模块的影响

            return true;
        }
        return false;
    }

    @Override
    public Boolean updateBlog(Blog blog) {

        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if(null==blogForUpdate){
            return false;
        }

        // 赋值属性
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());

        if(0==blog.getBlogCategoryId()){
            blogForUpdate.setBlogCategoryName("默认分类");
        }else{
            BlogCategory blogCategory = blogCategoryMapper.selectByCategoryId(blog.getBlogCategoryId());
            if(null!=blogCategory){
                blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            }else{
                blogForUpdate.setBlogCategoryName("默认分类");
                blogForUpdate.setBlogCategoryId(0);
            }
        }
        if(blogMapper.updateByPrimaryKeySelective(blog)>0){
            //对其他表的影响

            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteBlog(Integer[] ids) {
        if(ids.length<0){
            return false;
        }
        int result = blogMapper.deleteByPrimaryKeys(ids);
        if(result==ids.length){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<Blog> getBlogSortByTime(PageQueryUtil pageQueryUtil) {
        List<Blog> newestBlog = blogMapper.findNewestBlog(pageQueryUtil);
        return newestBlog;
    }

    @Override
    public List<Blog> getBlogSortByViews(PageQueryUtil pageQueryUtil) {
        List<Blog> mostViewsBlog = blogMapper.findMostViewsBlog(pageQueryUtil);
        return mostViewsBlog;
    }

    //以下三个函数为detail详情页实现
    //自己的实现方式
    @Override
    public BlogDetailVO getBlogdetailVOById(Long id) {
        BlogDetailVO blogDetailVOById = blogMapper.findBlogDetailVOById(id);
        return blogDetailVOById;
    }

    //项目实现
    @Override
    public BlogDetailVO getBlogDetail(Long id) {
        //直接选择来查找blog对象，不构建新对象的查找;
        Blog blog = blogMapper.selectByPrimaryKey(id);
        //不为空且状态为已发布——将blog对象处理成目标对象
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public BlogDetailVO  getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量——数据更新
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            //进行属性拷贝（浅拷贝）
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            //数据库里是text格式，需要转成markdown格式
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            //其他属性的赋值实现
            BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            //设置评论数
            Map params = new HashMap();
            params.put("blogId", blog.getBlogId());
            params.put("commentStatus", 1);
            //过滤审核通过的数据
            blogDetailVO.setCommentCount(blogCommentMapper.getTotalBlogComments(params));
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public PageResult getBlogPageByTagName(int page,String tagName) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",page);
        stringObjectHashMap.put("limit",5);
        stringObjectHashMap.put("tagName",tagName);

        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);

        List<Blog> blogs = blogMapper.selectBlogByTagName(pageQueryUtil);
        PageResult pageResult = new PageResult(blogs, blogs.size(), pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getBlogPageByCategoryName(int page,String categoryName) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",page);
        stringObjectHashMap.put("limit",5);
        stringObjectHashMap.put("categoryName",categoryName);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);

        List<Blog> blogs = blogMapper.selectBlogByCategoryName(pageQueryUtil);
//        for(Blog b:blogs){
//            System.out.println(b.getBlogId()+b.getBlogCategoryName());
//        }
        PageResult pageResult = new PageResult(blogs, blogs.size(), pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public BlogDetailVO getBlogDetailBySubUrl(String subUrl) {
        Blog blog = blogMapper.selectBySubUrl(subUrl);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public PageResult getBlogsByKeyword(PageQueryUtil pageQueryUtil) {
        List<Blog> blogs = blogMapper.selectBlogsByKeyword(pageQueryUtil);
        int total = blogs==null?0:blogs.size();
        PageResult pageResult = new PageResult(blogs, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }
}

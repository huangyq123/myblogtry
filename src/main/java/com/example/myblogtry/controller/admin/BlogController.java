package com.example.myblogtry.controller.admin;

import com.example.myblogtry.entity.Blog;
import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.service.BlogService;
import com.example.myblogtry.service.CategoryService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.Result;
import com.example.myblogtry.utils.ResultGenerator;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;

    @RequestMapping("/blogs")
    public String blogs(){
        return "admin/blog";
    }

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){

        if(StringUtils.isEmpty(params.get("page"))||StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数不能为空");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult( blogService.getBlogPage(pageQueryUtil));
    }


    @RequestMapping("/blogs/edit")
    public String save(HttpServletRequest request){
        //需要request，如果使用session会导致页面中找不到这个参数——为什么？？
        List<BlogCategory> allCategories = categoryService.getAllCategories();
        request.setAttribute("categories",allCategories);

        return "admin/edit";
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable("blogId") Long id){

//        System.out.println(id);
        Blog blogById = blogService.getBlogById(id);
        request.setAttribute("blog",blogById);

        List<BlogCategory> allCategories = categoryService.getAllCategories();
        request.setAttribute("categories",allCategories);

        return "admin/edit";
    }

    @PostMapping("/blogs/save")
    @ResponseBody
    public Result saveBlog(@RequestParam("blogTitle") String blogTitle,
                           @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                           @RequestParam("blogCategoryId") Integer blogCategoryId,
                           @RequestParam("blogTags") String blogTags,
                           @RequestParam("blogContent") String blogContent,
                           @RequestParam("blogCoverImage") String blogCoverImage,
                           @RequestParam("blogStatus") Byte blogStatus,
                           @RequestParam("enableComment") Byte enableComment){
        //参数验证
        //这些空的情况感觉不必要——提交请求时就已经验证了
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }

        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);

        Boolean aBoolean = blogService.saveBlog(blog);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("保存失败");
        }

    }

    @PostMapping("/blogs/update")
    @ResponseBody
    public Result updateBlog(@RequestParam("blogId") String blogId,
                             @RequestParam("blogTitle") String blogTitle,
                             @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                             @RequestParam("blogCategoryId") Integer blogCategoryId,
                             @RequestParam("blogTags") String blogTags,
                             @RequestParam("blogContent") String blogContent,
                             @RequestParam("blogCoverImage") String blogCoverImage,
                             @RequestParam("blogStatus") Byte blogStatus,
                             @RequestParam("enableComment") Byte enableComment){

        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }

        Blog blog = new Blog();
        blog.setBlogId(Long.parseLong(blogId));
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);

        Boolean aBoolean = blogService.updateBlog(blog);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("保存失败");
        }
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result deleteBlog(@RequestBody Integer[] ids){
        if(blogService.deleteBlog(ids)) {
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}

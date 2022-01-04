package com.example.myblogtry.controller.blog;

import com.example.myblogtry.controller.vo.BlogDetailVO;
import com.example.myblogtry.entity.Blog;
import com.example.myblogtry.entity.BlogComment;
import com.example.myblogtry.entity.BlogLink;
import com.example.myblogtry.service.*;
import com.example.myblogtry.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/")
public class MyblogController {

    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private ConfigService configService;
    @Resource
    private CommentService commentService;
    @Resource
    private LinkService linkService;


    @GetMapping({"/","/index",""})
    public String myblog(HttpServletRequest request){

        return this.list(request);
    }

//    @RequestMapping()
    public String list(HttpServletRequest request){

        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",1);
        stringObjectHashMap.put("limit",7);

        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);
        request.setAttribute("blogPageResult",blogService.getBlogPage(pageQueryUtil));

        request.setAttribute("hotTags",tagService.getBlogTagCountForIndex());

        stringObjectHashMap.put("lengthOfbLogs",5);
        request.setAttribute("newBlogs",blogService.getBlogSortByTime(pageQueryUtil));

        request.setAttribute("hotBlogs",blogService.getBlogSortByViews(pageQueryUtil));

        request.setAttribute("configurations",configService.getConfigList());

        return "blog/amaze/index";
    }

    //自己的实现
//    @GetMapping("/blog/{id}")
//    public String BlogContent(@PathVariable("id") Long id,HttpServletRequest request){
//        BlogDetailVO blogdetailVOById = blogService.getBlogdetailVOById(id);
//
//        Long page = (long) 1;
//        PageResult commentDetailRelateToBlog = commentService.getCommentDetailRelateToBlog(page, id);
//
//        request.setAttribute("configurations",configService.getConfigList());
//        request.setAttribute("blogDetailVO",blogdetailVOById);
//        request.setAttribute("commentPageResult",commentDetailRelateToBlog);
//        return "blog/amaze/detail";
//    }

//项目实现详情页处理
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", configService.getConfigList());
        return "blog/amaze/detail";
    }

    //热门标签页处理
    @RequestMapping("/tag/{tagName}")
    public String HotTags(@PathVariable("tagName") String tagName,HttpServletRequest request){

        PageResult blogPageByTagName = blogService.getBlogPageByTagName(1, tagName);

        request.setAttribute("blogPageResult",blogPageByTagName);

        request.setAttribute("hotTags",tagService.getBlogTagCountForIndex());

        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",1);
        stringObjectHashMap.put("limit",5);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);
        stringObjectHashMap.put("lengthOfbLogs",5);

        request.setAttribute("newBlogs",blogService.getBlogSortByTime(pageQueryUtil));

        request.setAttribute("hotBlogs",blogService.getBlogSortByViews(pageQueryUtil));

        request.setAttribute("configurations",configService.getConfigList());

        return "blog/amaze/index";

    }

    //项目实现——主要区别：使用新的页面；底部分页实现加了两个参数；由于需要分页，使用了第二种url；
//    /**
//     * 标签列表页
//     *
//     * @return
//     */
//    @GetMapping({"/tag/{tagName}"})
//    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName) {
//        return tag(request, tagName, 1);
//    }
//
//    /**
//     * 标签列表页
//     *
//     * @return
//     */
//    @GetMapping({"/tag/{tagName}/{page}"})
//    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") Integer page) {
//        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, page);
//        request.setAttribute("blogPageResult", blogPageResult);
//        request.setAttribute("pageName", "标签");
//        request.setAttribute("pageUrl", "tag");
//        request.setAttribute("keyword", tagName);
//        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
//        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
//        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
//        request.setAttribute("configurations", configService.getAllConfigs());
//        return "blog/" + theme + "/list";
//    }


    @RequestMapping("/category/{categoryName}")
    public String categoryResultList(@PathVariable("categoryName") String categoryName,HttpServletRequest request){
        int page =1;
        PageResult blogPageByCategoryName = blogService.getBlogPageByCategoryName(page, categoryName);

        request.setAttribute("blogPageResult",blogPageByCategoryName);

        request.setAttribute("hotTags",tagService.getBlogTagCountForIndex());

        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",1);
        stringObjectHashMap.put("limit",5);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);
        stringObjectHashMap.put("lengthOfbLogs",5);

        request.setAttribute("newBlogs",blogService.getBlogSortByTime(pageQueryUtil));

        request.setAttribute("hotBlogs",blogService.getBlogSortByViews(pageQueryUtil));

        request.setAttribute("configurations",configService.getConfigList());
        return "blog/amaze/list";
    }
//自己实现——区别：参数验证、方法冗余、辅助entity类使用；


    /**
     * 友情链接页
     *<没啥必要写></没啥必要写>
     * @return
     */
    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        request.setAttribute("configurations", configService.getConfigList());
        return "blog/amaze/link";
    }

    /**
     * 关于页面 以及其他配置了subUrl的文章页
     *
     * @return
     */
    @GetMapping({"/{subUrl}"})
    public String detail(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetailBySubUrl(subUrl);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", configService.getConfigList());
            return "blog/amaze/detail";
        } else {
            return "error/error_400";
        }
    }

    @PostMapping ("/blog/comment")
    @ResponseBody
    public Result publishComment(HttpServletRequest request, HttpSession session,
                                 @RequestParam Long blogId, @RequestParam String verifyCode,
                                 @RequestParam String commentator, @RequestParam String email,
                                 @RequestParam String websiteUrl, @RequestParam String commentBody){
        //自己考虑的参数验证
//        if(session.getAttribute("verifyCode")==null||!verifyCode.equals(session.getAttribute("verifyCode"))){
//            return ResultGenerator.genFailResult("验证码错误");
//        }

        //项目的参数验证
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        //这个干啥？？？
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }

        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        //邮箱格式的验证——怎么实现：使用正则匹配PatternUtil
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }

        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId);
//        blogComment.setCommentator(commentator);
//        blogComment.setWebsiteUrl(websiteUrl);
//        blogComment.setCommentBody(commentBody);
        blogComment.setEmail(email);

        //项目的额外处理——特殊形式的文本验证；文本格式处理;
        blogComment.setCommentator(MyBlogUtils.cleanString(commentator));
        if (PatternUtil.isURL(websiteUrl)) {
            blogComment.setWebsiteUrl(websiteUrl);
        }
        blogComment.setCommentBody(MyBlogUtils.cleanString(commentBody));

        Boolean aBoolean = commentService.addComment(blogComment);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("评论失败");
        }

    }

    @GetMapping("/search/{keyword}")
    public String searchBlog(@PathVariable("keyword") String keyword,HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        map.put("keyword",keyword);
        map.put("page",1);
        map.put("limit",5);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(map);

        PageResult blogsByKeyword = blogService.getBlogsByKeyword(pageQueryUtil);

        request.setAttribute("blogPageResult",blogsByKeyword);

        request.setAttribute("hotTags",tagService.getBlogTagCountForIndex());

        map.put("lengthOfbLogs",5);
        request.setAttribute("newBlogs",blogService.getBlogSortByTime(pageQueryUtil));

        request.setAttribute("hotBlogs",blogService.getBlogSortByViews(pageQueryUtil));

        request.setAttribute("configurations",configService.getConfigList());

        return "blog/amaze/index";
    }



}

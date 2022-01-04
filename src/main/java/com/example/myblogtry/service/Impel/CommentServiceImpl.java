package com.example.myblogtry.service.Impel;

import com.example.myblogtry.dao.BlogCommentMapper;
import com.example.myblogtry.entity.BlogComment;
import com.example.myblogtry.service.CommentService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Override
    public PageResult findCommentList(PageQueryUtil pageQueryUtil) {

        List<BlogComment> commentList = blogCommentMapper.findCommentList(pageQueryUtil);
        int total = commentList==null?0:commentList.size();
        PageResult pageResult = new PageResult(commentList, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean checkComments(Integer[] ids) {
        int length = blogCommentMapper.updateCommentsStatusByPrimaryKey(ids);
        if(length>0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean addReply(Long commentId, String replyBody) {
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(Math.toIntExact(commentId));
        if(null==blogComment||blogComment.getCommentStatus()==0){
            return false;
        }
        int length = blogCommentMapper.updateReplyBodyByPrimaryKey(commentId, replyBody);
        if(length>0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteComments(Integer[] ids) {
        int length = blogCommentMapper.deleteCommentsByPrimaryKey(ids);
        if(length>0){
            return true;
        }
        return false;
    }

    //自己实现  1
    @Override
    public PageResult getCommentDetailRelateToBlog(Long page,Long id) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("page",page);
        stringObjectHashMap.put("limit",3);
        stringObjectHashMap.put("blogId",id);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(stringObjectHashMap);

        List<BlogComment> commentsByBlogId = blogCommentMapper.findCommentsByBlogId(pageQueryUtil);
        PageResult pageResult = new PageResult(commentsByBlogId, commentsByBlogId.size(), pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    //项目实现  1
    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page) {
        if (page < 1) {
            return null;
        }
        Map params = new HashMap();
        params.put("page", page);
        //每页8条
        params.put("limit", 8);
        params.put("blogId", blogId);
        params.put("commentStatus", 1);//过滤审核通过的数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<BlogComment> comments = blogCommentMapper.findBlogCommentList(pageUtil);
        if (!CollectionUtils.isEmpty(comments)) {
            int total = blogCommentMapper.getTotalBlogComments(pageUtil);
            PageResult pageResult = new PageResult(comments, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        int result = blogCommentMapper.insertCommentWithBlogId(blogComment);
        if(result>0){
            return true;
        }
        return false;
    }
}

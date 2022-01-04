package com.example.myblogtry.service;

import com.example.myblogtry.entity.BlogComment;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentService {

    PageResult findCommentList(PageQueryUtil pageQueryUtil);

    Boolean checkComments(Integer[] ids);

    Boolean addReply(Long commentId, String replyBody);

    Boolean deleteComments(Integer[] ids);

    //自己实现
    PageResult getCommentDetailRelateToBlog(Long page,Long id);
    //项目实现
    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int page);

    Boolean addComment(BlogComment blogComment);
}

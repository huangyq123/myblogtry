package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogComment;
import com.example.myblogtry.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogCommentMapper {

    List<BlogComment>  findCommentList(PageQueryUtil pageQueryUtil);

    int updateCommentsStatusByPrimaryKey(Integer[] ids);

    int updateReplyBodyByPrimaryKey(@Param("commentId") Long commentId,@Param("replyBody") String replyBody);

    BlogComment selectByPrimaryKey(Integer id);

    int deleteCommentsByPrimaryKey(Integer[] ids);

    List<BlogComment> findCommentsByBlogId(PageQueryUtil pageQueryUtil);

    Integer getTotalBlogComments(Map params);

    List<BlogComment> findBlogCommentList(PageQueryUtil pageUtil);

    int insertCommentWithBlogId(BlogComment blogComment);

}

package com.example.myblogtry.controller.admin;


import com.example.myblogtry.service.CommentService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import com.example.myblogtry.utils.Result;
import com.example.myblogtry.utils.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/comments")
    public String comments(){
        return "admin/comment";
    }

    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params){
        if(StringUtils.isEmpty(params.get("page"))||StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数错误");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        PageResult commentList = commentService.findCommentList(pageQueryUtil);
        return ResultGenerator.genSuccessResult(commentList);
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result check(@RequestBody Integer[] ids){

        if(ids==null||ids.length<1){
            return ResultGenerator.genFailResult("参数验证失败");
        }

        Boolean aBoolean = commentService.checkComments(ids);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result addCommentsReply(@RequestParam("commentId") Long commentId,
                              @RequestParam("replyBody") String replyBody){
        if(commentId<1||replyBody==null){
            return ResultGenerator.genFailResult("参数验证失败");
        }
        Boolean aBoolean = commentService.addReply(commentId, replyBody);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("添加回复失败");
        }
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result deleteComments(@RequestBody Integer[] ids){
        if(ids==null||ids.length<1){
            return ResultGenerator.genFailResult("参数校验失败");
        }
        Boolean aBoolean = commentService.deleteComments(ids);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}

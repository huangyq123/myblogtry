package com.example.myblogtry.controller.admin;

import com.example.myblogtry.entity.BlogTag;
import com.example.myblogtry.service.TagService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import com.example.myblogtry.utils.Result;
import com.example.myblogtry.utils.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    @RequestMapping("/tags")
    public String tagPag(){
        return "admin/tag";
    }

    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){

        //参数验证
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        //项目实现——使用工具类来处理
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(tagService.getTagPage(pageUtil));

        //自己的实现
//        String page = (String) params.get("page");
//        String limit = (String) params.get("limit");
//        PageResult tagPage = tagService.getTagPage(page, limit);
//        return ResultGenerator.genSuccessResult(tagPage);
    }

    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(@RequestParam("tagName") String tagName){
        if(StringUtils.isEmpty(tagName)){
            return ResultGenerator.genFailResult("请输入标签名称");
        }

        boolean b = tagService.saveTag(tagName);
        if(b){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("保存失败");
        }
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public Result deleteTags(@RequestBody Integer[] ids){
        if(ids.length<=0){
            return ResultGenerator.genFailResult("请选择删除项");
        }
        boolean b = tagService.deleteTags(ids);
        if(b){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}

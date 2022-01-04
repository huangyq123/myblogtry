package com.example.myblogtry.controller.admin;

import com.example.myblogtry.entity.BlogLink;
import com.example.myblogtry.service.LinkService;
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
public class LinkController {

    @Resource
    private LinkService linkService;

    @RequestMapping("/links")
    public String Link(){
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params){
        if(StringUtils.isEmpty(params.get("page"))||StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数校验失败");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        PageResult linkList = linkService.findLinkList(pageQueryUtil);
        return ResultGenerator.genSuccessResult(linkList);
    }

    @PostMapping("/links/save")
    @ResponseBody
    public Result addLink(@RequestParam("linkType") Integer linkType,
                          @RequestParam("linkName") String linkName,
                          @RequestParam("linkUrl") String linkUrl,
                          @RequestParam("linkRank") Integer linkRank,
                          @RequestParam("linkDescription") String linkDescription){

        BlogLink blogLink = new BlogLink();
        //整形转换——这操作？？？？
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkName(linkName);
        blogLink.setLinkUrl(linkUrl);
        blogLink.setLinkRank(linkRank);
        blogLink.setLinkDescription(linkDescription);

        Boolean aBoolean = linkService.addLink(blogLink);
//        System.out.println(aBoolean);
        if(aBoolean){
            return ResultGenerator.genSuccessResult(aBoolean);
        }else{
            return ResultGenerator.genFailResult("增加失败");
        }
    }

}

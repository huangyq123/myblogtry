package com.example.myblogtry.controller.admin;

import com.example.myblogtry.entity.BlogCategory;
import com.example.myblogtry.service.CategoryService;
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
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categoryPage(){

        return "admin/category";
    }

    //查询显示数据请求
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){ //参数用map形式封装
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        //使用工具类包装用于查询的参数
        return ResultGenerator.genSuccessResult(categoryService.getBlogCategoryPage(pageQueryUtil));
        //调用service层实现查询，封装结果成需要的格式
    }

    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon){
        //考虑参数安全性
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }

        //这个参数为什么又不是map？？？
        Boolean aBoolean = categoryService.saveCategory(categoryName,categoryIcon);
        if(aBoolean==true){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon,
                         @RequestParam("categoryId") String categoryId){
        //考虑参数安全性
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }

        Boolean aBoolean = categoryService.updateCategory(categoryId,categoryName,categoryIcon);
        if(aBoolean){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("修改失败");
        }
    }


    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){  //为什么使用这个注解获取参数？？
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Boolean aBoolean = categoryService.deleteCategory(ids);
        if(aBoolean) {
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}

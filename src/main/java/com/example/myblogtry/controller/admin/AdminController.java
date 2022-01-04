package com.example.myblogtry.controller.admin;

import com.example.myblogtry.dao.AdminUserMapper;
import com.example.myblogtry.entity.AdminUser;
import com.example.myblogtry.service.AdminUserService;
import com.example.myblogtry.utils.MD5Util;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private AdminUserService adminUserService;

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session){

        //输入验证
        if (Strings.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (Strings.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }

        //查询数据库信息，查看用户是否存在
        AdminUser login = adminUserService.login(userName,password);

        if(null==login){
            //用户不存在
            session.setAttribute("errorMsg","用户名不存在");
                //该返回url还是modelmap？ url是绝对路径还是相对路径？
            return "admin/login";
        }else {
            //用户存在
            if(!MD5Util.MD5Encode(password,"UTF-8").equals(login.getLoginPassword())){
                //密码错误
                session.setAttribute("errorMsg","密码错误");
                return "admin/login";
            }else{
                //密码正确
                if(!verifyCode.equals(session.getAttribute("verifyCode"))){
                    //验证码错误
                    session.setAttribute("errorMsg","验证码错误");
                    return "admin/login";
                }else{
                    //所有信息正确
                    session.setAttribute("loginUser",login);
                    //重定向的返回？？
                    return "redirect:/admin/index";
                }
            }
        }
    }

    @GetMapping({"", "/", "/index", "/index.html"})
//    {"", "/", "/index", "/index.html"}
    public String successLogin(){
        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        //为什么使用request获取，不使用session获取？  作用范围？？
        AdminUser loginUser =(AdminUser) request.getSession().getAttribute("loginUser");
//        System.out.println(loginUser.getAdminUserId());
        String nickName = loginUser.getNickName();
//        String loginPassword = loginUser.getLoginPassword();
        String loginUserName = loginUser.getLoginUserName();

        request.setAttribute("path","profile");
        request.setAttribute("loginUserName",loginUserName);
        request.setAttribute("nickName",nickName);

        return "admin/profile";
    }

    @PostMapping("/profile/name")
    @ResponseBody //这个注解的作用——让返回值作为json字符串，而不是作为视图进行解析
    public String nameUpdate(@RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName,
                             HttpSession session,
                             HttpServletRequest request){
        AdminUser loginUser =(AdminUser) request.getSession().getAttribute("loginUser");
        Boolean aBoolean = adminUserService.updateName(loginUser.getAdminUserId(), loginUserName, nickName);
        if(aBoolean==true){
            return "success";
        }else{
            return "修改失败";
        }
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(@RequestParam("originalPassword")String originalPassword,
                                 @RequestParam("newPassword")String newPassword,
                                 HttpServletRequest request){
        AdminUser loginUser =(AdminUser) request.getSession().getAttribute("loginUser");
        //普通版本
//        if(originalPassword.equals(loginUser.getLoginPassword())){
//            Boolean aBoolean = adminUserService.updatePassword(loginUser.getAdminUserId(), newPassword);
//            if(aBoolean){
//                //成功修改
//                request.getSession().removeAttribute("loginUser");//已有信息已经失效
//                return "success";
//            }else{
//                //失败
//                return "修改失败";
//            }
//        }else{
//            //原密码错误
//            return "修改失败";
//        }

        //md5加密版本

        Boolean aBoolean = adminUserService.updatePassword(loginUser.getAdminUserId(),originalPassword,newPassword);
        if(aBoolean){
            //成功修改
            request.getSession().removeAttribute("loginUser");//已有信息已经失效
            return "success";
        }else{
            //失败
            return "修改失败";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");

        return "admin/login";
    }
}

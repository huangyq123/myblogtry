package com.example.myblogtry.service.Impel;

import com.example.myblogtry.dao.AdminUserMapper;
import com.example.myblogtry.entity.AdminUser;
import com.example.myblogtry.service.AdminUserService;
import com.example.myblogtry.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
//        System.out.println(loginUserId);
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
//        System.out.println(adminUser);
        if(adminUser!=null){
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
//            System.out.println(adminUser);
            int i = adminUserMapper.updateByPrimaryKeySelective(adminUser);
            if(i>0){
                return true;
            }else{
//                System.out.println("i<=0");
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String password) {
        //普通版本
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        if(adminUser!=null){
            //存在用户
            adminUser.setLoginPassword(password);
            if(adminUserMapper.updateByPrimaryKeySelective(adminUser)>0){
                //修改成功
                return true;
            }else{
                //失败
                return false;
            }
        }else{
            //不存在用户
            return false;
        }

    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword,String password) {
        //md5版本
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        if(adminUser!=null) {
            //存在用户
            String original = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPassword = MD5Util.MD5Encode(password, "UTF-8");
            if (original.equals(adminUser.getLoginPassword())) {
                //原密码正确
                adminUser.setLoginPassword(newPassword);
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    //修改成功
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AdminUser login(String userName, String password) {
        String newPassword = MD5Util.MD5Encode(password, "UTF-8");
        AdminUser login = adminUserMapper.login(userName, newPassword);
        return login;
    }
}

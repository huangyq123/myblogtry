package com.example.myblogtry.service;


import com.example.myblogtry.entity.AdminUser;

public interface AdminUserService {

    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

    Boolean updatePassword(Integer loginUserId, String password);

    public Boolean updatePassword(Integer loginUserId, String originalPassword,String password);

    public AdminUser login(String userName, String password);
}

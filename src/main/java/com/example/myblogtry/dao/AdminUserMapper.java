package com.example.myblogtry.dao;

import com.example.myblogtry.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface AdminUserMapper {

    public AdminUser login(@Param("userName") String userName,@Param("password") String password);

    public AdminUser loginForName(@Param("userName") String userName);

    public int updateByPrimaryKeySelective(AdminUser record);

    public AdminUser selectByPrimaryKey(Integer adminUserId);
}

package com.example.myblogtry.config;

import com.example.myblogtry.Interceptor.AdminLoginInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MyBlogWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry){
        //配置一个拦截器对指定路径进行拦截
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**").excludePathPatterns("/admin/plugins/**");
        //将拦截器进行注册
//        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //为了避免进行自定义拦截器进行拦截时对静态资源访问的拦截，对 WebMvcConfigurationSupport 进行继承会导致的效果；
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }



}

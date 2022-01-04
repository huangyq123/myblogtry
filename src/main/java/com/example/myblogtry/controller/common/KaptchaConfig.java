package com.example.myblogtry.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        //使用这个类来生成一个生产者，这个生产者用于产生验证码
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //验证的外形配置，也可以使用properties文件进行配置，这进行硬编码固定了格式
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.image.width", "150");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.textproducer.font.size", "30");
        properties.put("kaptcha.session.key", "verifyCode");
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        //通过这种配置的使用规范了验证码的格式
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}

////Kaptcha配置
//@Configuration
//public class KaptchaConfig {
//    @Bean
//    public DefaultKaptcha producer() {
//        //Properties类
//        Properties properties = new Properties();
//        // 图片边框
//        properties.setProperty("kaptcha.border", "yes");
//        // 边框颜色
//        properties.setProperty("kaptcha.border.color", "105,179,90");
//        // 字体颜色
//        properties.setProperty("kaptcha.textproducer.font.color", "blue");
//        // 图片宽
//        properties.setProperty("kaptcha.image.width", "110");
//        // 图片高
//        properties.setProperty("kaptcha.image.height", "40");
//        // 字体大小
//        properties.setProperty("kaptcha.textproducer.font.size", "30");
//        // session key
//        properties.setProperty("kaptcha.session.key", "code");
//        // 验证码长度
//        properties.setProperty("kaptcha.textproducer.char.length", "4");
//        // 字体
//        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
//        //图片干扰
//        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.DefaultNoise");
//        //Kaptcha 使用上述配置
//        Config config = new Config(properties);
//        //DefaultKaptcha对象使用上述配置, 并返回这个Bean
//        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
//        defaultKaptcha.setConfig(config);
//        return defaultKaptcha;
//    }
//}
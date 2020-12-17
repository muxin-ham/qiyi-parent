package com.muxin.project.config;

import com.muxin.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProjectConfig {
    //加载配置文件中的oss属性
    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OSSTemplate ossTemplate(){
        return new OSSTemplate();
    }
}

package com.smartfilemanager.config;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
public class AppConfig {
    private String watchedPath = System.getProperty("user.home"); // 默认为用户主目录
}

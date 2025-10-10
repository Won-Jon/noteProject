package com.suixin.noteproject;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 这是Servlet初始化器，用于将Spring Boot应用打包成WAR文件部署到外部Servlet容器（如Tomcat）时的初始化配置
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NoteProjectApplication.class);
    }

}

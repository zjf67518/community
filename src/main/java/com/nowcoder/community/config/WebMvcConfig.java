package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.*;
import com.nowcoder.community.entity.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.jpg", "/img/*.png", "/img/*.jpeg")
                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.jpg", "/img/*.png", "/img/*.jpeg");

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.jpg", "/img/*.png", "/img/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.jpg", "/img/*.png", "/img/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/css/*.css", "/js/*.js", "/img/*.jpg", "/img/*.png", "/img/*.jpeg");
    }
}

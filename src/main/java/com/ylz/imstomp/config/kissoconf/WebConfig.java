package com.ylz.imstomp.config.kissoconf;

import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * <p>
 * WEB 初始化相关配置
 * </p>
 *
 */
@ControllerAdvice
@Configuration
@ConditionalOnProperty(prefix = "im-stomp.eva", name = "kisso", havingValue = "true")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // kisso 拦截器配置
        registry.addInterceptor(new SSOSpringInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }
}

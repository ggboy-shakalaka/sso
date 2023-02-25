package com.zhaizq.sso.config;

import com.zhaizq.sso.sdk.SsoFilter;
import com.zhaizq.sso.sdk.domain.SsoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring 拦截器
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
    }

    // Spring 参数转换器
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new DateConverter());
    }

    // Spring 静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Autowired
    private SsoConfig ssoConfig;

    //    @Bean
    public FilterRegistrationBean<SsoFilter> ssoFilter() throws IOException {
        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SsoFilter(ssoConfig));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }
}
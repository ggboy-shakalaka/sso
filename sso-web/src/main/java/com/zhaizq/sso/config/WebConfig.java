package com.zhaizq.sso.config;

import com.zhaizq.sso.sdk.SsoFilter;
import com.zhaizq.sso.sdk.SsoService;
import com.zhaizq.sso.sdk.domain.SsoConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Bean
    public FilterRegistrationBean<SsoFilter> ssoFilter() {
        SsoConfig ssoConfig = new SsoConfig();
        SsoService ssoService = new SsoService(ssoConfig);
        SsoFilter ssoFilter = new SsoFilter(ssoService);

        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(ssoFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }
}
package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoApi;
import cn.zhaizq.sso.sdk.SsoFilter;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            // Spring 拦截器
            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
            }

            // Spring 参数转换器
            public void addFormatters(FormatterRegistry registry) {
//                registry.addConverter(new DateConverter());
            }

            // Spring 静态资源映射
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/view/static/");
            }
        };
    }

    @Autowired
    private SsoConfig ssoConfig;

    @Bean
    public SsoApi ssoApi() throws IOException {
        return SsoApi.init(ssoConfig);
    }

    @Bean
    public FilterRegistrationBean<SsoFilter> ssoFilter(SsoApi ssoApi) throws IOException {
        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SsoFilter(ssoApi));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }

    @Autowired
    private RedisProperties redisConfig;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(6000);
        return new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword(), redisConfig.getDatabase());
    }
}
package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoFilter;
import cn.zhaizq.sso.sdk.SsoService;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

@Configuration
public class WebConfig {

    @Value("${sso.project.request.mapping.prefix}")
    private String requestMappingPrefix;

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
//                registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
            }

//            @Override
//            public void configurePathMatch(PathMatchConfigurer configurer) {
//                configurer.addPathPrefix(requestMappingPrefix, t -> true);
//            }
        };
    }

//    @Autowired
//    private SsoConfig ssoConfig;
//
//    @Bean
//    public SsoService ssoService() throws Exception {
//        return new SsoService(ssoConfig);
//    }

//    @Bean
//    public FilterRegistrationBean<SsoFilter> ssoFilter(SsoService ssoService) throws IOException {
//        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new SsoFilter(ssoService));
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.setEnabled(true);
//        return filterRegistrationBean;
//    }

    @Autowired
    private RedisProperties redisConfig;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(6000);
        return new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword(), redisConfig.getDatabase());
    }
}
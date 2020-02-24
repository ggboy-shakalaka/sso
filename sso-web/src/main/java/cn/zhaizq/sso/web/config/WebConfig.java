package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.SsoFilter;
import com.ggboy.framework.utils.redis.RedisWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class WebConfig {
    @Value("${sso.server}")
    private String server;
    @Value("${sso.appId}")
    private String appId;
    @Value("${sso.ignore}")
    private String ignore;

    @Bean
    public FilterRegistrationBean<SsoFilter> ssoFilter() {
        FilterRegistrationBean<SsoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SsoFilter());
        filterRegistrationBean.addInitParameter(SsoConstant.SERVER_PATH, server);
        filterRegistrationBean.addInitParameter(SsoConstant.APP_ID, appId);
        filterRegistrationBean.addInitParameter(SsoConstant.IGNORE_PATH, ignore);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public RedisWrapper redisWrapper() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, host, port, timeout, password, database);
        return new RedisWrapper(jedisPool);
    }
}
package cn.zhaizq.sso.web.config;

import cn.zhaizq.sso.sdk.SsoApi;
import cn.zhaizq.sso.sdk.SsoFilter;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

@Configuration
public class WebConfig {
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
package com.zhaizq.sso.service.service;

import com.zhaizq.sso.service.domain.entry.Application;
import com.zhaizq.sso.service.mapper.ApplicationMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationMapper applicationMapper;

    public Application query(String appId) {
        if (true) {
            Application application = new Application();
            return application;
        }

        QueryWrapper<Application> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        return applicationMapper.selectOne(query);
    }
}
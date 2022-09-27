package com.zhaizq.sso.service;

import com.zhaizq.sso.mapper.entry.Application;
import com.zhaizq.sso.mapper.ApplicationMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationMapper applicationMapper;

    public Application query(String appId) {
        QueryWrapper<Application> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        return applicationMapper.selectOne(query);
    }
}
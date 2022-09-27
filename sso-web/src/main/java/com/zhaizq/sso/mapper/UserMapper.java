package com.zhaizq.sso.mapper;

import com.zhaizq.sso.mapper.entry.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
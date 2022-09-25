package com.zhaizq.sso.service.domain.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("basic_user")
public class User {
    private Integer id;
    private String userName;
    private String password;
    private Date createTime;
}
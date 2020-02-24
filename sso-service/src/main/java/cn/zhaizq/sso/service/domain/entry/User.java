package cn.zhaizq.sso.service.domain.entry;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String userName;
    private String password;
}
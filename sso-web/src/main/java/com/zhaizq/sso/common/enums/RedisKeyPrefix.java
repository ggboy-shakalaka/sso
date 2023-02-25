package com.zhaizq.sso.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKeyPrefix {
    login_user_private_key("Login:UserPrivateKey:");
    private final String prefix;
}
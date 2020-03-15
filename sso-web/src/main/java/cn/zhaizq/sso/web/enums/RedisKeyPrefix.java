package cn.zhaizq.sso.web.enums;

public enum RedisKeyPrefix {
    login_user_private_key("Login:UserPrivateKey:")
    ;

    private String prefix;
    RedisKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
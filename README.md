# 单点登录

## Nginx代理注意事项
```
location / {
    proxy_set_header    host    $host;
    proxy_pass  http://127.0.0.1:8080;
}
```
或
```
# 替换你自己的域名
sso.local.path=https://zhaizq.cn
```
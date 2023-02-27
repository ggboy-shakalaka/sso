package com.zhaizq.sso.sdk.abc;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RefreshTokenHandler {
    public void action(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 302);
        map.put("path", "http://sso.com/uncheck/refreshToken");
        map.put("redirect", "http://www.com/setToken");
        response.getWriter().write(JSON.toJSONString(map));
    }
}
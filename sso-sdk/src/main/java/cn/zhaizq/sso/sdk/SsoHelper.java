package cn.zhaizq.sso.sdk;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SsoHelper {
    public static boolean isMatch(String pattern, String path) {
        if (path == null || pattern == null)
            return false;

        path = path.trim();
        pattern = pattern.trim();

        return path.equals(pattern) || path.startsWith(pattern + "/");
    }

    public static Cookie getSsoToken(HttpServletRequest request) {
        if (request == null || request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if (SsoConstant.TOKEN_NAME.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    public static String getSsoTokenString(HttpServletRequest request) {
        Cookie cookie = getSsoToken(request);
        return cookie == null ? null : cookie.getValue();
    }

    public static void setSsoToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(SsoConstant.TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static String getRootPath(HttpServletRequest request) {
        return request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length());
    }
}

package cn.zhaizq.sso.sdk;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SsoFilter implements Filter {

    private String server;

    public void init(FilterConfig filterConfig) throws ServletException {
        server = filterConfig.getInitParameter(Conf.a);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }

    public static class Conf {
        public final static String a = "";
        public final static String b = "";
        public final static String c = "";
        public final static String d = "";
        public final static String e = "";
    }
}
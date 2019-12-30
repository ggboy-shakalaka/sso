package cn.zhaizq.sso.sdk;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SsoFilter implements Filter {

    public final static String login_path = "/login";
    public final static String logout_path = "/logout";

    private String appId;
    private String server;
    private String login;
    private String logout;
    private String ignore;

    public void init(FilterConfig filterConfig) throws ServletException {
        appId = filterConfig.getInitParameter(Conf.APP_ID);
        server = filterConfig.getInitParameter(Conf.SERVER_PATH);
        login = filterConfig.getInitParameter(Conf.LOGIN_PATH);
        logout = filterConfig.getInitParameter(Conf.LOGOUT_PATH);
        ignore = filterConfig.getInitParameter(Conf.IGNORE_PATH);

        login = login == null || login.length() == 0 ? "/login" : login;
        logout = logout == null || logout.length() == 0 ? "/logout" : logout;
        ignore = ignore == null ? "" : ignore;
        ignore += "/favicon.ico";
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();

        if (requestUri.startsWith(login)) {
            response.sendRedirect(server + login_path);
            return;
        }

        if (requestUri.startsWith(logout)) {
            response.sendRedirect(server + logout_path);
            return;
        }

        for (String ignoreUrl : ignore.split(",")) {
            if (requestUri.startsWith(ignoreUrl)) {
                System.out.println("ignore uri: " + requestUri);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        System.out.println(request.getRequestURI());
//
//        response.sendRedirect(server + logout);
//        Cookie[] cookies = request.getCookies();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }

    public static class Conf {
        public final static String APP_ID = "APP_ID";
        public final static String SERVER_PATH = "SERVER_PATH";
        public final static String LOGIN_PATH = "LOGIN_PATH";
        public final static String LOGOUT_PATH = "LOGOUT_PATH";
        public final static String IGNORE_PATH = "IGNORE_PATH";
    }
}
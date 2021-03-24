package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SsoFilter implements Filter {
    private final SsoConfig ssoConfig;
    private final SsoService ssoService;

    public SsoFilter(SsoConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
        this.ssoService = new SsoService(ssoConfig);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String token = request.getParameter(SsoConstant.TOKEN_NAME);
        if (token != null) {
            response.addCookie(new Cookie(SsoConstant.TOKEN_NAME, token));
            response.sendRedirect(request.getRequestURI()); // TODO remove url param(token) and redirect url
            return;
        }

        String requestUri = request.getRequestURI();
        if (ssoService.isMatchIgnore(requestUri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie tokenCookie = getCookie(request, SsoConstant.TOKEN_NAME);
        SsoResponse resp = ssoService.checkToken(tokenCookie);

        if (resp.getCode() != 200) {
            // redirect to Refresh Token
            String redirect = request.getRequestURL() + "?" + request.getQueryString();
            String redirectUrl = "http://sso.com/uncheck/refresh?appKey=" + ssoConfig.getAppId() + "&redirect=" + URLEncoder.encode(redirect, "UTF-8");
            response.sendRedirect(redirectUrl);
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (request == null || request.getCookies() == null || cookieName == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}
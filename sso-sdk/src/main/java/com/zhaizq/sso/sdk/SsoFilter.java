package com.zhaizq.sso.sdk;

import com.zhaizq.sso.sdk.domain.SsoConfig;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import lombok.AllArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class SsoFilter implements Filter {
    private final SsoService ssoService;

    public SsoFilter(SsoConfig ssoConfig) {
        this.ssoService = new SsoService(ssoConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (ssoService.isMatchIgnore(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = request.getParameter(SsoConstant.TOKEN_NAME);
        if (token != null) {
            response.addCookie(new Cookie(SsoConstant.TOKEN_NAME, token));
            response.sendRedirect(request.getRequestURI()); // TODO remove url param(token) and redirect url
            return;
        }

        SsoResponse resp = ssoService.checkToken(request);
        if (resp == null || resp.getCode() != 200) {
            response.sendRedirect(ssoService.buildRedirectUrl(request.getRequestURL().toString()));
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
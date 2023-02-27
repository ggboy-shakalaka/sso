package com.zhaizq.sso.sdk;

import com.zhaizq.sso.sdk.abc.RefreshTokenHandler;
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
    private final RefreshTokenHandler refreshTokenHandler = new RefreshTokenHandler();

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

        if (ssoService.isMatchSetToken(request.getRequestURI())) {
            String token = request.getParameter(SsoConstant.TOKEN_NAME);
            String source = request.getParameter(SsoConstant.SOURCE_NAME);
            response.addCookie(new Cookie(SsoConstant.TOKEN_NAME, token));
            response.sendRedirect(source != null ? source : "/");
            return;
        }

        SsoResponse resp = ssoService.checkToken(request);
        if (resp == null || resp.getCode() != 200) {
            refreshTokenHandler.action(request, response);
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
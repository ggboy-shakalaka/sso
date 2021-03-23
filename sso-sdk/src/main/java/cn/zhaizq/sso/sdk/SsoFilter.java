package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoFilter implements Filter {
    private final SsoService ssoService;

    public SsoFilter(SsoConfig ssoConfig) {
        this.ssoService = new SsoService(ssoConfig);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        Cookie tokenCookie = SsoHelper.getSsoToken(request);
        SsoHelper.setSsoToken(response, request.getParameter(SsoConstant.TOKEN_NAME));
        if (tokenCookie != null && !tokenCookie.isHttpOnly())
            response.addCookie(tokenCookie);

        if (ssoService.isMatchIgnore(requestUri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getParameter(SsoConstant.TOKEN_NAME) != null) {
            // remove url param(token) and redirect url
            SsoHelper.setSsoToken(response, request.getParameter(SsoConstant.TOKEN_NAME));
            response.sendRedirect(request.getParameter(SsoConstant.REDIRECT) == null ? "/" : request.getParameter(SsoConstant.REDIRECT));
            return;
        }

        SsoResponse resp = ssoService.checkToken(tokenCookie == null ? null : tokenCookie.getValue());

        if (resp.getCode() != 200) {
            // redirect to Refresh Token
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
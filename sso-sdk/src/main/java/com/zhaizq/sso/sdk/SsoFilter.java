package com.zhaizq.sso.sdk;

import com.zhaizq.sso.sdk.domain.SsoResponse;
import lombok.AllArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class SsoFilter implements Filter {
    private final SsoService ssoService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (ssoService.isNotMatch(request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (ssoService.isMatchSetToken(request.getRequestURI())) {
            String token = request.getParameter(SsoConstant.TOKEN_NAME);
            String source = request.getParameter(SsoConstant.SOURCE_NAME);
            SsoHelper.setSsoToken(response, token);
            response.sendRedirect(source != null ? source : "/");
            return;
        }

        SsoResponse resp = ssoService.checkToken(request);
        if (resp == null || resp.getCode() != 200) {
            ssoService.doOnTokenExpire(request, response);
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
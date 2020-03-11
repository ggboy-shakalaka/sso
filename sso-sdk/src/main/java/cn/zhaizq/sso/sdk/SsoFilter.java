package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoFilter implements Filter {
    private SsoApi ssoApi;

    public SsoFilter(SsoApi ssoApi) {
        this.ssoApi = ssoApi;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        String token = SsoHelper.getSsoToken(request);

        for (String ignoreUrl : ssoApi.getConfig().getIgnore()) {
            if (SsoHelper.isMatch(ignoreUrl, requestUri)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (request.getParameter(SsoConstant.TOKEN_NAME) != null) {
            SsoHelper.setSsoToken(response, request.getParameter(SsoConstant.TOKEN_NAME));
            response.sendRedirect(request.getParameter(SsoConstant.REDIRECT) == null ? "/" : request.getParameter(SsoConstant.REDIRECT));
            return;
        }

        SsoResponse<SsoUser> resp = ssoApi.checkToken(token);

        if (resp.getCode() != 200) {
            response.sendRedirect(ssoApi.getConfig().getRefreshTokenUrl(SsoHelper.getRootPath(request)));
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}
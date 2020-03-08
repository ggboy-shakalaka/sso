package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoFilter implements Filter {
    private String ignore;

    private SsoService ssoService;

    public void init(FilterConfig filterConfig) throws ServletException {
        String appId = filterConfig.getInitParameter(SsoConstant.APP_ID);
        String server = filterConfig.getInitParameter(SsoConstant.SERVER_PATH);
//        login = filterConfig.getInitParameter(Conf.LOGIN_PATH);
//        logout = filterConfig.getInitParameter(Conf.LOGOUT_PATH);
        ignore = filterConfig.getInitParameter(SsoConstant.IGNORE_PATH);

//        login = login == null || login.length() == 0 ? "/login" : login;
//        logout = logout == null || logout.length() == 0 ? "/logout" : logout;
        ignore = ignore == null ? "" : ignore;

        ssoService = new SsoService(server, appId);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        String token = SsoHelper.getSsoToken(request);

//        if (SsoHelper.isMatch(login, requestUri)) {
//            response.sendRedirect(ssoService.getLoginPath());
//            return;
//        }
//
//        if (SsoHelper.isMatch(logout, requestUri)) {
//            response.sendRedirect(ssoService.getLogoutPath());
//            return;
//        }

        for (String ignoreUrl : ignore.split(",")) {
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

        SsoResponse<SsoUser> resp = ssoService.checkToken(token);

        if (resp.code() != 200) {
            response.sendRedirect(ssoService.getRefreshTokenPath(SsoHelper.getRootPath(request), null));
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.data());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
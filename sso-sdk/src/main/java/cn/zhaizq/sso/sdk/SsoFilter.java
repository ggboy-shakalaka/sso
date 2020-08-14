package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import lombok.SneakyThrows;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SsoFilter implements Filter {
    private SsoService ssoService;

    public SsoFilter(SsoService ssoService) {
        this.ssoService = ssoService;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        String token = SsoHelper.getSsoToken(request);

        if (ssoService.isMatchIgnore(requestUri)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getParameter(SsoConstant.TOKEN_NAME) != null) {
            SsoHelper.setSsoToken(response, request.getParameter(SsoConstant.TOKEN_NAME));
            response.sendRedirect(request.getParameter(SsoConstant.REDIRECT) == null ? "/" : request.getParameter(SsoConstant.REDIRECT));
            return;
        }

        SsoResponse resp = ssoService.checkToken(token);

        if (resp.getCode() != 200) {
            response.sendRedirect("/");
//            response.sendRedirect(ssoApi.getConfig().getRefreshTokenUrl(SsoHelper.getRootPath(request)));
            return;
        }

        request.setAttribute(SsoConstant.SSO_USER, resp.getData());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @SneakyThrows
    public void init(FilterConfig filterConfig) throws ServletException {
//        ssoService.helloWorld();
    }

    public void destroy() {
    }
}
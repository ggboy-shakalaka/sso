package com.zhaizq.sso.controller;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/uncheck/**")
public class UncheckController extends BaseController {
    @RequestMapping
    public void abc() throws IOException {
        String redirect = request.getParameter("redirect");
        URIBuilder uriBuilder = new URIBuilder(URI.create(redirect));
        response.sendRedirect(uriBuilder.addParameter("sso.token", UUID.randomUUID().toString()).toString());
    }
}

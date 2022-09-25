package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.controller.BaseController;
import com.zhaizq.sso.sdk.SsoHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/haha")
public class HaHa extends BaseController {
    @RequestMapping("/test")
    public void refresh_token() throws IOException {
        System.out.println(SsoHelper.getSsoToken(request));
        SsoHelper.setSsoToken(response, "abcdefg");
    }

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://zbwms.hnqst.cn");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String s = EntityUtils.toString(execute.getEntity());
        System.out.println(s);
    }
}
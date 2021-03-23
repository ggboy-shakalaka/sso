package cn.zhaizq.sso.web.controller.api;

import cn.zhaizq.sso.sdk.SsoHelper;
import cn.zhaizq.sso.web.controller.BaseController;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
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
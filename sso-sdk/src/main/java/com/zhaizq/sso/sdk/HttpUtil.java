package com.zhaizq.sso.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    public static String doPost(String url, String data) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json; charset=utf8");
        headers.put("Content-Type", "application/json; charset=utf8");
        return doRequest(url, "POST", headers, data);
    }

    public static String postJson(String url, String data) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json; charset=utf8");
        headers.put("Content-Type", "application/json; charset=utf8");
        return doRequest(url, "POST", headers, data);
    }

    public static String doRequest(String url, String method, Map<String, String> headers, String body) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        byte[] bytes = doRequest(url, method, headers, body != null ? body.getBytes(charset) : null);
        return new String(bytes, charset);
    }

    public static byte[] doRequest(String url, String method, Map<String, String> headers, byte[] body) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            urlConnection.setRequestMethod(method);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (body != null) {
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(body);
                outputStream.flush();
            }

            urlConnection.connect();

            int length = urlConnection.getContentLength();
            ByteArrayOutputStream out = length > 0 ? new ByteArrayOutputStream(length) : new ByteArrayOutputStream();

            int offset;
            byte[] buffer = new byte[8 * 1024];
            InputStream in = urlConnection.getInputStream();
            while ((offset = in.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }

            return out.toByteArray();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
}
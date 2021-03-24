package cn.zhaizq.sso.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {
    public static String doPost(String url, String data) throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/text; charset=utf8");
            urlConnection.setRequestProperty("Content-Type", "application/text; charset=utf8");

            if (data != null) {
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(data.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            urlConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line);

            return stringBuilder.toString();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
}
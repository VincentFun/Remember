package com.example.meihui.remember.utils;

import org.apache.http.HttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

/**
 * Created by meihui on 2016/4/11.
 */
public class URLHelper {

    public static String getResultFromUrlPath(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return getResult(connection);

    }

    public static String postDataWithResult(String urlString,byte[] data) throws IOException {
        URL url=new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        // 设置允许输出
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        // 设置User-Agent: Fiddler
        connection.setRequestProperty("ser-Agent", "Fiddler");
        // 设置contentType
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os=connection.getOutputStream();
        os.write(data);
        os.flush();

        return getResult(connection);

    }

    private static String getResult(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        StringBuilder result = new StringBuilder();
        String buf = null;
        while ((buf = br.readLine()) != null) {
            result.append(buf);
        }
        return result.toString();
    }

}

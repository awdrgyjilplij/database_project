package com.example.databaseproject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerConnector {

    void request(String path, final String output,
                 final RecallFunction recall,
                 final String method, String address) {
        final String urlString = address + path;
        new Thread(() -> {
            HttpURLConnection conn = null;
            PrintWriter writer = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setRequestMethod(method);

                if (method.equals("POST")) {
                    conn.setRequestProperty("content-type",
                            "application/json");
                    conn.setDoOutput(true);
                    writer = new PrintWriter(new OutputStreamWriter(
                            conn.getOutputStream(),
                            StandardCharsets.UTF_8
                    ));
                    writer.print(output);
                    writer.flush();
                    writer.close();
                }

                int responseCode = conn.getResponseCode(); // To Check for 200
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Logger.d("RINE_debug", "responseCode error:" + responseCode);
                }

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                if (recall != null) {
                    recall.call(response.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }

    void request(String path, final String output,
                 final RecallFunction recall,
                 final String method) {
        request(path, output, recall, method,
                "http://152.136.159.228:3001/");
    }

    void post(String path, final String output,
              final RecallFunction recall) {
        request(path, output, recall, "POST");
    }

    void get(String path, final RecallFunction recall) {
        request(path, "", recall, "GET");
    }

    void post(String path, final String output) {
        post(path, output, null);
    }


}
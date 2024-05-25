package com.dtstep.lighthouse.common.util;

import okhttp3.*;

import java.io.IOException;

public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public static String post(String url, String requestBody) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),requestBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}

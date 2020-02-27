package com.gonzaloandcompany.satapp.retrofit;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class InterceptorToken implements Interceptor {
    private String authToken;

    public InterceptorToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder().header("Authorization", "Bearer " + authToken);

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}

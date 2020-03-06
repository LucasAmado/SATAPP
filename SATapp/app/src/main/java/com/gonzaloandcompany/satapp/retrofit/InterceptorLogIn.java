package com.gonzaloandcompany.satapp.retrofit;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class InterceptorLogIn implements Interceptor {
    private String authToken;

    public InterceptorLogIn(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder().header("Authorization", authToken);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
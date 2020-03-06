package com.gonzaloandcompany.satapp.retrofit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiSAT {

    private static final String BASE_URL = "https://heroku-satapp.herokuapp.com/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createServicePeticiones (Class<S> serviceClass, final String authToken) {

        if (retrofit==null) {

            if(authToken!=null){
                httpClient.addInterceptor(new InterceptorToken(authToken));
            }
            builder.client(httpClient.build());

            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}

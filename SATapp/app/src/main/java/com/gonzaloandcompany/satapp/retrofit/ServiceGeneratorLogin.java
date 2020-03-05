package com.gonzaloandcompany.satapp.retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGeneratorLogin {

    private static final String BASE_URL = "https://heroku-satapp.herokuapp.com/";
    public static String MASTER_KEY = "grupo1masterkey";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = null;
    private static TipoAutenticacion tipoActual = null;

    // Interceptor que imprime por el Log todas las peticiones y respuestas
    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClientBuilder =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (!(username.isEmpty() || password.isEmpty())) {
            String credentials = username +":"+ password;
            Log.d(credentials, "credenciales");
            return createService(serviceClass, credentials);
        }
        return createService(serviceClass, null);
    }


    public static <S> S createService(Class<S> serviceClass, final String authtoken) {

        if (retrofit == null) {

            httpClientBuilder.interceptors().clear();

            httpClientBuilder.addInterceptor(logging);

                httpClientBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalUrl = original.url();

                        HttpUrl url = originalUrl.newBuilder()
                                .addQueryParameter("access_token", MASTER_KEY)
                                .build();

                        Request request = original.newBuilder()
                                .url(url)
                                .build();

                        return chain.proceed(request);
                    }
                });
            }
            retrofit= builder.build();
        return retrofit.create(serviceClass);
    }


}

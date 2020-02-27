package com.gonzaloandcompany.satapp.services;

import com.gonzaloandcompany.satapp.models.Login;
import com.gonzaloandcompany.satapp.models.Register;
import com.gonzaloandcompany.satapp.models.Usuario;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RegisterService {
    @POST("/users?access_token=")
    Call<Register> registerUser(@Part MultipartBody.Part avatar,
                                @Part ("email") RequestBody email,
                                @Part ("password") RequestBody password,
                                @Query("access_token") String token);

    @GET("/auth?access_token=")
    Call<Login> loginUser();

}

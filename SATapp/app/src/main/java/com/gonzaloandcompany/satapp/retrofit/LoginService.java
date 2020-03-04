package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Login;
import com.gonzaloandcompany.satapp.mymodels.Register;
import com.gonzaloandcompany.satapp.mymodels.Users;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoginService {

    @POST("/users?access_token=")
    Call<Register> userRegister(@Part MultipartBody.Part avatar,
                                @Part ("email") RequestBody email,
                                @Part ("password") RequestBody password,
                                @Query("access_token") String token);

    @POST ("/auth?access_token=")
    Call<Login> loginUser(@Query("user") Users user, @Query("token") String token);
}
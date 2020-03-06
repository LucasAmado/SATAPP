package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.Register;
import com.gonzaloandcompany.satapp.mymodels.Users;
import com.gonzaloandcompany.satapp.ui.login.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoginService {

    @POST("/users")
    Call<Register> userRegister(@Part MultipartBody.Part avatar,
                                @Part ("email") RequestBody email,
                                @Part ("password") RequestBody password,
                                @Query("access_token") String token);
    @Multipart
    @POST("/users")
    Call<Users> userRegister( @Part ("email") RequestBody email,
                              @Part ("password") RequestBody password,
                              @Part ("name") RequestBody name,
                              @Part MultipartBody.Part avatar);

    @POST ("/auth")
    Call<LoginResponse> loginUser();
}

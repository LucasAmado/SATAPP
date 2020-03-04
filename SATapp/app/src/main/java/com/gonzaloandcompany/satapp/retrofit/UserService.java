package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    @GET("/users")
    Call<List<UsuarioDummy>> getUsers();

    @GET("/users")
    Call<List<UsuarioDummy>> getUsersPaginable(@Query("page")int page, @Query("limit") int limit);
}

package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("/users")
    Call<List<UsuarioDummy>> getUsers();

    @GET("/users")
    Call<List<UsuarioDummy>> getUsersPaginable(@Query("page")int page, @Query("limit") int limit);

    @GET("/users/{id}")
    Call<UsuarioDummy> getUser(@Path("id") String id);

    @DELETE("/users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @PUT("/users/{id}/tecnico")
    Call<UsuarioDummy> promote(@Path("id") String id);

    @PUT("/users/{id}/validate")
    Call<UsuarioDummy> validate(@Path("id") String id);

    @GET("/users/me")
    Call<UsuarioDummy> getCurrentUser();
}

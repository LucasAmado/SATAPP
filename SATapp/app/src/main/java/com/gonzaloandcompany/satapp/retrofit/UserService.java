package com.gonzaloandcompany.satapp.retrofit;

import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("/users")
    Call<List<UsuarioDummy>> getUsers();
}

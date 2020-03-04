package com.gonzaloandcompany.satapp.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService service;
    private MutableLiveData<List<UsuarioDummy>> users = new MutableLiveData<>();
    private MutableLiveData<List<UsuarioDummy>> paginables = new MutableLiveData<>();

    private MutableLiveData<UsuarioDummy> user = new MutableLiveData<>();
    private MutableLiveData<UsuarioDummy> res = new MutableLiveData<>();

    public UserRepository() {
        this.service = ApiSAT.createServicePeticiones(UserService.class, Constants.TOKEN_PROVISIONAL);
    }

    public LiveData<List<UsuarioDummy>> getUsers() {
        Call<List<UsuarioDummy>> call = service.getUsers();
        call.enqueue(new Callback<List<UsuarioDummy>>() {
            @Override
            public void onResponse(Call<List<UsuarioDummy>> call, Response<List<UsuarioDummy>> response) {
                if (response.isSuccessful()) {
                    users.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioDummy>> call, Throwable t) {

            }
        });
        return users;
    }

    public LiveData<List<UsuarioDummy>> getUsersPaginable(int page, int limit) {
        Call<List<UsuarioDummy>> call = service.getUsersPaginable(page, limit);
        call.enqueue(new Callback<List<UsuarioDummy>>() {
            @Override
            public void onResponse(Call<List<UsuarioDummy>> call, Response<List<UsuarioDummy>> response) {
                if (response.isSuccessful()) {
                    paginables.setValue(response.body());
                    Log.d("PAGINABLES",paginables.getValue().toString());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioDummy>> call, Throwable t) {

            }
        });
        return paginables;
    }

    public LiveData<UsuarioDummy> getUser(String id) {
        Call<UsuarioDummy> call = service.getUser(id);
        call.enqueue(new Callback<UsuarioDummy>() {
            @Override
            public void onResponse(Call<UsuarioDummy> call, Response<UsuarioDummy> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDummy> call, Throwable t) {

            }
        });
        return user;
    }

    public void deleteUser(String id){
        Call<Void> call = service.deleteUser(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("DELETE USER",id+" SUCCESSFUL");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DELETE USER",id+" FAILURE");
            }

        });

    }

    public LiveData<UsuarioDummy> promote(String id) {
        final MutableLiveData<UsuarioDummy> data = new MutableLiveData<>();
        Call<UsuarioDummy> call = service.promote(id);
        call.enqueue(new Callback<UsuarioDummy>() {
            @Override
            public void onResponse(Call<UsuarioDummy> call, Response<UsuarioDummy> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDummy> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<UsuarioDummy> validate(String id) {
        final MutableLiveData<UsuarioDummy> data = new MutableLiveData<>();
        Call<UsuarioDummy> call = service.validate(id);
        call.enqueue(new Callback<UsuarioDummy>() {
            @Override
            public void onResponse(Call<UsuarioDummy> call, Response<UsuarioDummy> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDummy> call, Throwable t) {

            }
        });
        return data;
    }
}

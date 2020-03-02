package com.gonzaloandcompany.satapp.data.repository;

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
    UserService service;

    public UserRepository() {
        this.service = ApiSAT.createServicePeticiones(UserService.class, Constants.TOKEN_PROVISIONAL);
    }

    public LiveData<List<UsuarioDummy>> getUsers() {
        final MutableLiveData<List<UsuarioDummy>> data = new MutableLiveData<>();
        Call<List<UsuarioDummy>> call = service.getUsers();
        call.enqueue(new Callback<List<UsuarioDummy>>() {
            @Override
            public void onResponse(Call<List<UsuarioDummy>> call, Response<List<UsuarioDummy>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioDummy>> call, Throwable t) {

            }
        });
        return data;
    }
}

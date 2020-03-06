package com.gonzaloandcompany.satapp.data.repository;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.common.MyApp;
import com.gonzaloandcompany.satapp.requests.EditUsers;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.Password;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.LoginServiceGenerator;
import com.gonzaloandcompany.satapp.retrofit.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService service;
    private UserService loginServise;
    private MutableLiveData<List<UsuarioDummy>> users = new MutableLiveData<>();
    private MutableLiveData<List<UsuarioDummy>> paginables = new MutableLiveData<>();
    private String nombre;
    private MutableLiveData<UsuarioDummy> user = new MutableLiveData<>();
    private MutableLiveData<UsuarioDummy> res = new MutableLiveData<>();

    public UserRepository() {
        this.service = ApiSAT.createServicePeticiones(UserService.class, Constants.TOKEN_PROVISIONAL);
        this.loginServise = LoginServiceGenerator.createService(UserService.class);
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

    public void updateUser(String id, EditUsers name) {
        Call<EditUsers> call = service.updateUser(id, name);
        call.enqueue(new Callback<EditUsers>() {
            @Override
            public void onResponse(Call<EditUsers> call, Response<EditUsers> response) {
                if (response.isSuccessful()) {
                    Log.d("Update USER", id + " SUCCESSFUL");
                }
            }

            @Override
            public void onFailure(Call<EditUsers> call, Throwable t) {

            }
        });
    }

    public void updatePassword(String email,String paaswordOld,Password passwordNew,String id) {
        String base = email + ":" + paaswordOld;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<Password> call = loginServise.updatePassword(authHeader ,id, passwordNew);
        call.enqueue(new Callback<Password>() {
            @Override
            public void onResponse(Call<Password> call, Response<Password> response) {
                if (response.isSuccessful()) {
                    Log.d("Update PASSWORD", " SUCCESSFUL");
                    Toast.makeText(MyApp.getContext(), "Contraseña cambiada correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Error al cambiar contraseña", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Password> call, Throwable t) {
                Log.d("Update PASSWORD"," FAILURE");
                Toast.makeText(MyApp.getContext(), "Error al cambiar contraseña", Toast.LENGTH_LONG).show();
            }
        });
    }

    public LiveData<UsuarioDummy> getMyUser() {
        Call<UsuarioDummy> call = service.getMyUser();
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

    public LiveData<UsuarioDummy> getCurrentUser() {
        final MutableLiveData<UsuarioDummy> data = new MutableLiveData<>();
        Call<UsuarioDummy> call = service.getMyUser();
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

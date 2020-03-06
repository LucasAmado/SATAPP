package com.gonzaloandcompany.satapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Users;
import com.gonzaloandcompany.satapp.retrofit.LoginService;
import com.gonzaloandcompany.satapp.retrofit.ServiceGeneratorLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editTextNameRegistro)
    EditText nameRegistro;

    @BindView(R.id.editTextEmailRegistro)
    EditText emailRegistro;

    @BindView(R.id.editTextPasswordRegistro)
    EditText passwordRegistro;

    @BindView(R.id.editTextComprobarPasswordRegistro)
    EditText passwordRegistroComprobar;

    @BindView(R.id.buttonRegistrarse)
    Button registrarse;

    String token;

    LoginService service = ServiceGeneratorLogin.createServiceRegister(LoginService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        registrarse.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               if(!nameRegistro.getText().toString().isEmpty() && !emailRegistro.getText().toString().isEmpty()
                       && !passwordRegistro.getText().toString().isEmpty()
                       && passwordRegistro.getText().toString().compareTo(passwordRegistroComprobar.getText().toString())==0) {

                   RequestBody name = RequestBody.create(MultipartBody.FORM, nameRegistro.getText().toString());
                   RequestBody email = RequestBody.create(MultipartBody.FORM, emailRegistro.getText().toString());
                   RequestBody password = RequestBody.create(MultipartBody.FORM, passwordRegistro.getText().toString());
                   Call<Users> callRegister = service.userRegister(email, password, name, null);

                   callRegister.enqueue(new Callback<Users>() {
                       @Override
                       public void onResponse(Call<Users> call, Response<Users> response) {
                           if (!response.isSuccessful()){
                               Log.d("RESPONSE REGISTER", response.message());
                               Toast.makeText(RegisterActivity.this, "No se ha podido registrar al nuevo usuario", Toast.LENGTH_SHORT).show();
                           }
                           else{
                               Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                               startActivity(i);
                           }
                       }
                       @Override
                       public void onFailure(Call<Users> call, Throwable t) {
                           Log.e("FALLO", t.getMessage());
                           Toast.makeText(RegisterActivity.this, "Fallo en la llamada a la API", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
               else{
                   Toast.makeText(RegisterActivity.this, "Hay algun campo en blanco o las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
}

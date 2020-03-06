package com.gonzaloandcompany.satapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gonzaloandcompany.satapp.MainActivity;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.retrofit.LoginService;
import com.gonzaloandcompany.satapp.retrofit.ServiceGeneratorLogin;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.buttonLogin)
    Button btLogin;

    @BindView(R.id.textViewRegistro)
    TextView tvRegistro;

    @BindView(R.id.editTextEmail)
    EditText email;

    @BindView(R.id.editTextPassword)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btLogin.setOnClickListener(this);
        tvRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                String emailUser = email.getText().toString();
                String passwordUser = password.getText().toString();

                LoginService service = ServiceGeneratorLogin.createService(LoginService.class, emailUser, passwordUser);
                Call<LoginResponse> call = service.loginUser();
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.code() != 201) {
                            Toast.makeText(LoginActivity.this, "No ha podido iniciar sesion", Toast.LENGTH_SHORT).show();
                        }
                            else {
                                Intent i = new Intent(LoginActivity.this , MainActivity.class);
                                startActivity(i);
                        }
                            Log.e("RESPUES PETICION", ""+response);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("NetworkFailure", t.getMessage());
                        Toast.makeText(LoginActivity.this, "Fallo en la llamada a la API", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.textViewRegistro:
                startActivity(new Intent(LoginActivity.this ,RegisterActivity.class));
                break;
        }
    }

    public void onLoginCorrect(JSONObject json, String msg) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(i);
    }

}

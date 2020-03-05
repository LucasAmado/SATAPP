package com.gonzaloandcompany.satapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gonzaloandcompany.satapp.MainActivity;
import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.Login;
import com.gonzaloandcompany.satapp.mymodels.Users;
import com.gonzaloandcompany.satapp.retrofit.LoginService;
import com.gonzaloandcompany.satapp.retrofit.ServiceGeneratorLogin;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.buttonLogin)
    Button btLogin;

    @BindView(R.id.buttonRegistro)
    Button btRegistro;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                String emailUser = email.getText().toString();
                String passwordUser = password.getText().toString();

                String credentials = Credentials.basic(emailUser, passwordUser);
                LoginService service = ServiceGeneratorLogin.createService(LoginService.class);

                break;
            case R.id.buttonRegistro:
                Intent i = new Intent(LoginActivity.this ,RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    public void onLoginCorrect(JSONObject json, String msg) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(i);
    }

}

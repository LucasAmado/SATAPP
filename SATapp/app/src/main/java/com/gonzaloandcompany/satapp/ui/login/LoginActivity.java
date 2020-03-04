package com.gonzaloandcompany.satapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Login;
import com.gonzaloandcompany.satapp.mymodels.Users;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                Login login = new Login(new Users(emailUser), passwordUser);
                break;
            case R.id.buttonRegistro:
                Intent i = new Intent(LoginActivity.this ,RegisterActivity.class);
                startActivity(i);
                break;
        }
    }
}

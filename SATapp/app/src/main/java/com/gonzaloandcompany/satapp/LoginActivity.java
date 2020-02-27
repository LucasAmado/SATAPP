package com.gonzaloandcompany.satapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gonzaloandcompany.satapp.models.Login;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editTextEmail)
    EditText email;
    @BindView(R.id.editTextPassword)
    EditText password;
    @BindView(R.id.buttonLogin)
    Button login;
    @BindView(R.id.buttonRegister)
    Button register;

    EditText getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        ButterKnife.bind(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

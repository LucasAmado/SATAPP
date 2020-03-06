package com.gonzaloandcompany.satapp.mymodels;

public class Login {
    private UsuarioDummy user;
    private String token;

    public Login(UsuarioDummy user, String token) {
        this.user = user;
        this.token = token;
    }

    public UsuarioDummy getUser() {
        return user;
    }

    public void setUser(UsuarioDummy user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

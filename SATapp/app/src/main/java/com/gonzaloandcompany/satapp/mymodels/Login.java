package com.gonzaloandcompany.satapp.mymodels;

public class Login {
    private Users user;
    private String token;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

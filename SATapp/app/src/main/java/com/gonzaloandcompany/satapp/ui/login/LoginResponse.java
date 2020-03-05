package com.gonzaloandcompany.satapp.ui.login;

import com.gonzaloandcompany.satapp.mymodels.Users;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private Users user;


    public LoginResponse() { }

    public LoginResponse(String token, Users user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginResponse that = (LoginResponse) o;

        if (!token.equals(that.token)) return false;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}

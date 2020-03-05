package com.gonzaloandcompany.satapp.mymodels;

public class Register {
    private String avatar;
    private String name;
    private String email;
    private String password;

    public Register(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

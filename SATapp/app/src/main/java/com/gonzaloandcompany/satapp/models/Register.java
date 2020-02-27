package com.gonzaloandcompany.satapp.models;

public class Register {
    private String name;
    private String email;
    private String password;
    private String avatar;
    private String role;

    public Register() { }

    public Register(String name, String email, String password, String picture, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = picture;
        this.role = role;
    }

    public Register(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "user";
    }
}

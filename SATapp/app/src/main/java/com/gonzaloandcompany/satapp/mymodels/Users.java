package com.gonzaloandcompany.satapp.mymodels;

public class Users {
    private String id;
    private String email;
    private String name;
    private String role;
    private String picture;
    private String password;
    private String createdAt;
    private String updatedAt;
    private String avatar;
    private Boolean Validated;

    public Users(String emailUser) {
    }

    public Users(String avatar, String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getValidated() {
        return Validated;
    }

    public void setValidated(Boolean validated) {
        Validated = validated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.gonzaloandcompany.satapp.requests;

public class EditUsers {
    private String name;

    public EditUsers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

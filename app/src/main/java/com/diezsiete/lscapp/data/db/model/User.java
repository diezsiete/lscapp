package com.diezsiete.lscapp.data.db.model;


public class User {
    private String token;
    private String profileId;
    private String email;
    private String password;
    private String confirmPassword;

    public User(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileId() {
        return profileId;
    }
}

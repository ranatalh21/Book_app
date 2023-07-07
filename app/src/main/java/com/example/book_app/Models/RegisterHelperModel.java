package com.example.book_app.Models;

public class RegisterHelperModel {

    String name, username,email,password,token;


    public RegisterHelperModel() {
    }

    public RegisterHelperModel( String name, String username, String email, String password, String token) {

        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.token = token;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

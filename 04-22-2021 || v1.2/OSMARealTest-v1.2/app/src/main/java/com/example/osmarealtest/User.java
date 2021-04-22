package com.example.osmarealtest;

public class User {
    private String username, password, email, account;

    public User(String username, String password, String email, String account) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.account = account;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        this.username = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

}


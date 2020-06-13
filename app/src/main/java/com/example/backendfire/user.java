package com.example.backendfire;

public class user {
    private String email;
    private String username;
    public user(String email,String username) {
        this.email = email;
        this.username = username;
    }
    public user(){}
    public void setEmail(String email){
        this.email=email;
    }

    public void setUsername(String username){
        this.username=username;
    }
    public String getEmail(){
        return this.email;
    }
    public String getUsername(){
        return this.username;
    }

}

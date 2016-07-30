package com.example.saar.locationalert;

/**
 * Created by Saar on 1/9/2016.
 */
public class User {
    String name, userName, password;

    public User(String name, String userName, String password){
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.name = "";
    }



}

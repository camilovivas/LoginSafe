package com.example.loginsafe.model;

import java.util.Calendar;


public class User {
//usuario unico, contraseña, ultima hora de ingreso
    private String userName;
    private String password;
    private Calendar lastCheckInTime;

    public User(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(Calendar lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }
}

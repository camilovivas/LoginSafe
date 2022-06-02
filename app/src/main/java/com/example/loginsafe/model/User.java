package com.example.loginsafe.model;

import java.io.Serializable;
import java.util.Calendar;


public class User implements Serializable {
//usuario unico, contraseña, ultima hora de ingreso
    private String userName;
    private String password;
    private Long lastCheckInTime;

    public User(){

    }

    public User(String userName, String password, Long lastCheckInTime) {
        this.userName = userName;
        this.password = password;
        this.lastCheckInTime = lastCheckInTime;
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

    public Long getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(Long lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }
}

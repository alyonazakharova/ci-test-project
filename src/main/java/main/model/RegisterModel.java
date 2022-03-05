package main.model;

import java.io.Serializable;

public class RegisterModel implements Serializable {
    private String userName;
    private String password;
    private String name;
    private String phone;

    public RegisterModel(String userName, String password, String phone, String name) {
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

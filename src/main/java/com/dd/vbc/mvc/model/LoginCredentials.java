package com.dd.vbc.mvc.model;

import java.io.Serializable;
import java.util.Arrays;


public class LoginCredentials extends Serialization implements Serializable {

    private static final long serialVersionUID = 5673732176670753862L;
    private String username;
    private String password;

    public LoginCredentials() {}
    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] serialize() {

        return concatenateBytes(serializeString(username), serializeString(password));
    }

    public void deserialize(byte[] bytes) {

        int index = 0;
        if(index<bytes.length) {
            int usernameLength = deserializeInt(Arrays.copyOfRange(bytes, index, index + 4));
            username = deserializeString(Arrays.copyOfRange(bytes, index, index = index + usernameLength+4));
        }
        if(index<bytes.length) {
            int passwordLength = deserializeInt(Arrays.copyOfRange(bytes, index, index + 4));
            password = deserializeString(Arrays.copyOfRange(bytes, index, index + passwordLength+4));
        }
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

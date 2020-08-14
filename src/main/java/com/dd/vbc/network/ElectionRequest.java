package com.dd.vbc.network;

import com.dd.vbc.enums.Request;
import com.dd.vbc.mvc.model.LoginCredentials;
import com.dd.vbc.mvc.model.Serialization;
import com.dd.vbc.mvc.model.Voter;

import java.io.Serializable;
import java.util.Arrays;

public class ElectionRequest extends Serialization implements Serializable {

    private Request request;
    private LoginCredentials loginCredentials;
    private Voter voter;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public LoginCredentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public byte[] serialize() {

        byte[] bytes = null;
        switch(request) {
            case Login: {
                if(loginCredentials!=null) {
                    bytes = concatenateBytes(request.serialize(), loginCredentials.serialize());
                    break;
                }
            }
            case BallotRequest: {
                if(voter!=null) {
                    bytes = concatenateBytes(request.serialize(), voter.serialize());
                    break;
                }
            }
        }
        return bytes;
    }

    public void deserialize(byte[] byteRequest) {

        request = Request.fromOrdinal(deserializeInt(Arrays.copyOfRange(byteRequest, 0, 4)));
        switch(request) {
            case Login: {
                loginCredentials = new LoginCredentials();
                loginCredentials.deserialize(Arrays.copyOfRange(byteRequest, 4, byteRequest.length));
                break;
            }
            case BallotRequest: {
                voter = new Voter();
                voter.deserialize(Arrays.copyOfRange(byteRequest, 4, byteRequest.length));
                break;
            }
        }
    }
}

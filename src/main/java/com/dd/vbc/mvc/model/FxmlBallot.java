package com.dd.vbc.mvc.model;

public class FxmlBallot extends Serialization {

    private byte[] file;

    public FxmlBallot() {}
    public FxmlBallot(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}

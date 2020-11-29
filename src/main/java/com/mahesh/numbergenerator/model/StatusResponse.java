package com.mahesh.numbergenerator.model;

public class StatusResponse {

    public enum STATUS {SUCCESS, IN_PROGRESS, ERROR}

    private STATUS result;

    public STATUS getResult() {
        return result;
    }

    public void setResult(STATUS result) {
        this.result = result;
    }

}

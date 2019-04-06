package com.horcrux.shareandgo.models;

import java.io.Serializable;

public class MessageData implements Serializable {

    String number;
    String message;

    public MessageData() {
    }

    public MessageData(String number, String message) {
        this.number = number;
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

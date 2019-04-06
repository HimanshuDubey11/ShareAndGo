package com.horcrux.shareandgo.models;

import java.io.Serializable;

public class PhoneCall implements Serializable {

    String number;
    String state;

    public PhoneCall() {

    }

    public PhoneCall(String number, String state) {
        this.number = number;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

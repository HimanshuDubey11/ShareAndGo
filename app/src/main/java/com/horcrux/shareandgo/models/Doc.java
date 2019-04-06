package com.horcrux.shareandgo.models;

import java.io.Serializable;

public class Doc implements Serializable {

    String title;
    String data;

    public Doc() {
    }

    public Doc(String title, String data) {
        this.title = title;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

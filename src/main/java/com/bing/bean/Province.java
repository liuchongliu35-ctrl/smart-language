package com.bing.bean;

import lombok.Data;

import java.util.Arrays;

public class Province {
    private String name;
    private String value;
    private String[] tipData;

    public Province() {
    }

    public Province(String name, String value, String[] tipData) {
        this.name = name;
        this.value = value;
        this.tipData = tipData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getTipData() {
        return tipData;
    }

    public void setTipData(String... tipData) {
        this.tipData = tipData;
    }

    @Override
    public String toString() {
        return "Province{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", tipData=" + Arrays.toString(tipData) +
                '}';
    }
}

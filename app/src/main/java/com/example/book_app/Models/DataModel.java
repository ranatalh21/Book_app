package com.example.book_app.Models;

public class DataModel {
    private String dataImage;
    private String dataName;
    private String dataDesc;
    private String publishBY;
    private String Key;
     private String token;

    public DataModel() {
    }



    public DataModel(String dataImage, String dataName, String dataDesc, String publishBY, String key) {
        this.dataImage = dataImage;
        this.dataName = dataName;
        this.dataDesc = dataDesc;
        this.publishBY = publishBY;
        Key = key;

    }





    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getPublishBY() {
        return publishBY;
    }

    public void setPublishBY(String publishBY) {
        this.publishBY = publishBY;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "dataImage='" + dataImage + '\'' +
                ", dataName='" + dataName + '\'' +
                ", dataDesc='" + dataDesc + '\'' +
                ", publishBY='" + publishBY + '\'' +
                ", Key='" + Key + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
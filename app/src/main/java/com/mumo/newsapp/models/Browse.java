package com.mumo.newsapp.models;

public class Browse {
    String image;
    String textTitle;
    String textSubTitle;


    public Browse(String image, String textTitle, String textSubTitle) {
        this.image = image;
        this.textTitle = textTitle;
        this.textSubTitle = textSubTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public String getTextSubTitle() {
        return textSubTitle;
    }

    public void setTextSubTitle(String textSubTitle) {
        this.textSubTitle = textSubTitle;
    }
}

package com.bing.service.pictureRecognition;


public interface PictureToText {
    public String photoToText(String photoPath);
    public String photoToTextWithMoreLanguage(String photoPath);
}

package com.nptel.courses.online.entities;

import java.util.ArrayList;

public class User {

    private String androidId;
    private ArrayList<String> favouriteVideos;

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public ArrayList<String> getFavouriteVideos() {
        return favouriteVideos;
    }

    public void setFavouriteVideos(ArrayList<String> favouriteVideos) {
        this.favouriteVideos = favouriteVideos;
    }
}

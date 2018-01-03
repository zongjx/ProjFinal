package com.lab.zongjx.projfinal;

import android.graphics.Bitmap;

public class ChatItem {
    private Bitmap photo;
    private String name;

    public ChatItem(Bitmap photo, String name){
        this.photo = photo;
        this.name = name;
    }

    public void setPhoto(Bitmap photo){
        this.photo = photo;
    }

    public void setName(String name){
        this.name = name;
    }

    public Bitmap getPhoto(){
        return photo;
    }

    public String getName(){
        return name;
    }
}

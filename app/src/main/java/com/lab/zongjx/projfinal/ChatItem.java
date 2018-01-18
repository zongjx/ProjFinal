package com.lab.zongjx.projfinal;

import android.graphics.Bitmap;

public class ChatItem {
    private Bitmap photo;
    private String name;
    private String from;

    public ChatItem(Bitmap photo, String name, String from){
        this.photo = photo;
        this.name = name;
        this.from = from;
    }

    public void setPhoto(Bitmap photo){
        this.photo = photo;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFrom(String from) {this.from = from;}

    public Bitmap getPhoto(){
        return photo;
    }

    public String getName(){
        return name;
    }

    public String getFrom() {return from;}
}

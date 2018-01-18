package com.lab.zongjx.projfinal;

import android.app.Dialog;
import android.graphics.Bitmap;

public class DialogItem {
    private String dialog;
    private String time;
    private Bitmap photo;
    private boolean own;

    public DialogItem(String dialog, String time, Bitmap photo, boolean own){
        this.dialog = dialog;
        this.time = time;
        this.photo = photo;
        this.own = own;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getDialog() {
        return dialog;
    }

    public String getTime() {
        return time;
    }

    public boolean getOwn() {return own;}

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setOwn(boolean own) {this.own = own;}
}

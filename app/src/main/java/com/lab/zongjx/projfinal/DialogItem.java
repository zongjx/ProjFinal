package com.lab.zongjx.projfinal;

import android.app.Dialog;
import android.graphics.Bitmap;

public class DialogItem {
    private String dialog;
    private String time;
    private Bitmap photo;

    public DialogItem(String dialog, String time, Bitmap photo){
        this.dialog = dialog;
        this.time = time;
        this.photo = photo;
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

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.lab.zongjx.projfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyDialogAdapter extends BaseAdapter {

    private List<DialogItem> list = null;
    private Context context = null;

    public MyDialogAdapter(Context context,List<DialogItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        if(list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public View getView(int i, View v, ViewGroup parent){
        v = LayoutInflater.from(context).inflate(R.layout.dialog_chat,parent,false);
        TextView dialog = (TextView) v.findViewById(R.id.dialog_chat);
        TextView time = (TextView) v.findViewById(R.id.time_chat);
        ImageView photo = (ImageView) v.findViewById(R.id.photo_dialog);
        dialog.setText(list.get(i).getDialog());
        time.setText(list.get(i).getTime());
        photo.setImageBitmap(list.get(i).getPhoto());
        return v;
    }

    @Override
    public DialogItem getItem(int i){
        if(list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    public void removeItem(int position){
        list.remove(position);
    }

}
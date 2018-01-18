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
        TextView dialog1 = (TextView) v.findViewById(R.id.dialog_chat1);
        TextView time = (TextView) v.findViewById(R.id.time_chat);
        ImageView photo1 = (ImageView) v.findViewById(R.id.photo_dialog1);
        ImageView photo2 = (ImageView) v.findViewById(R.id.photo_dialog2);
        TextView dialog2 = (TextView) v.findViewById(R.id.dialog_chat2);
        if(list.get(i).getOwn()){
            photo2.setImageBitmap(list.get(i).getPhoto());
            dialog2.setText(list.get(i).getDialog());
            photo1.setVisibility(View.GONE);
            dialog1.setVisibility(View.GONE);
        }
        else{
            photo1.setImageBitmap(list.get(i).getPhoto());
            dialog1.setText(list.get(i).getDialog());
            photo2.setVisibility(View.GONE);
            dialog2.setVisibility(View.GONE);
        }
        time.setText(list.get(i).getTime());
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
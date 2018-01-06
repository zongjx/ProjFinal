package com.lab.zongjx.projfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MySearchAdapter extends BaseAdapter {

    private List<ChatItem> list = null;
    private Context context = null;

    public MySearchAdapter(Context context,List<ChatItem> list) {
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
        v = LayoutInflater.from(context).inflate(R.layout.searchitem_search,parent,false);
        TextView nickname = (TextView) v.findViewById(R.id.name_search);
        ImageView photo = (ImageView) v.findViewById(R.id.photo_search);
        nickname.setText(list.get(i).getName());
        photo.setImageBitmap(list.get(i).getPhoto());
        return v;
    }

    @Override
    public ChatItem getItem(int i){
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

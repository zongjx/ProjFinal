package com.lab.zongjx.projfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyDialogAdapter extends BaseAdapter {

    private List<String[]> list = null;
    private Context context = null;

    public MyDialogAdapter(Context context,List<String[]> list) {
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
        TextView item = (TextView) v.findViewById(R.id.dialog_chat);
        TextView time = (TextView) v.findViewById(R.id.time_chat);
        item.setText(list.get(i)[0]);
        time.setText(list.get(i)[1]);
        return v;
    }

    @Override
    public String[] getItem(int i){
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
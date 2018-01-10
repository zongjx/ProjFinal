package com.lab.zongjx.projfinal;

import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ASUS on 2018/1/8.
 */

public class javabean {
    protected ArrayList<Map<String,Object>> mData;
    protected String teammates_name;
    protected int num1;
    protected int teammates_num;
    protected boolean flag_join;
    protected boolean flag_quit;
    protected boolean flag_clear;
    protected SimpleAdapter adapter;
    public javabean(){
        mData = new ArrayList<Map<String, Object>>();
        teammates_name = new String();
        num1=teammates_num=0;
        flag_join=flag_quit=flag_clear=false;
    }
    public void setmData(Map<String,Object> item){
        mData.add(item);
    }
    public void setTeammates_name(String s){
        teammates_name = s;
    }
    public void setNum1(int i){
        num1 = i;
    }
    public void setTeammates_num(int i){
        teammates_num = i;
    }
    public void setFlag_join(boolean flag){
        flag_join = flag;
    }
    public void setFlag_quit(boolean flag){
        flag_quit = flag;
    }
    public void setFlag_clear(boolean flag){
        flag_clear = flag;
    }
    public void setAdapter(SimpleAdapter a){
        adapter = a;
    }

    public void clear_mdata(){
        mData.clear();
    }
    public void remove_mdata(int i){
        mData.remove(i);
    }

    public ArrayList<Map<String,Object>>  getmData(){
        return mData;
    }
    public String getTeammates_name(){
        return teammates_name;
    }
    public int getNum1(){
        return num1;
    }
    public int getTeammates_num(){
        return teammates_num;
    }
    public boolean getFlag_join(){
        return flag_join;
    }
    public boolean getFlag_quit(){
        return flag_quit;
    }
    public boolean getFlag_clear(){
        return flag_clear;
    }
    public SimpleAdapter getAdapter(){
        return adapter;
    }
}

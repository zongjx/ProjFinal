package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyChatAdapter extends BaseSwipeAdapter {

    private List<ChatItem> list;
    private Context mContext;

    public MyChatAdapter(Context mContext,List<ChatItem> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.chatitem_chat,parent,false);
        TextView name = (TextView) v.findViewById(R.id.name_chat);
        ImageView photo = (ImageView) v.findViewById(R.id.photo_chat);
        photo.setImageBitmap(list.get(position).getPhoto());
        name.setText(list.get(position).getName());
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.maincontext);
                alertDialog.setTitle("删除")
                        .setMessage("确认要删除这个对话吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(position);
                                Thread threadsearch = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(100);
                                            Class.forName("com.mysql.jdbc.Driver");
                                        } catch (InterruptedException e) {
                                            Log.v("ss", e.toString());
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        String ip = "120.78.73.208";
                                        int port = 3306;
                                        String dbName = "zuazu";
                                        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                                        String USER = "root";
                                        String PASSWORD = "123456";

                                        try {
                                            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                                            Log.v("ss", "success");
                                            String sql = "delete from chat where from_who = '" + list.get(position).getFrom() + "' and to_who = '" + list.get(position).getName() + "';";
                                            Statement st = (Statement) conn.createStatement();
                                            st.executeUpdate(sql);
                                            st.close();
                                            conn.close();
                                        } catch (SQLException e) {
                                            Log.v("ss", "fail");
                                            Log.v("ss", e.getMessage());
                                        }
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            Log.v("ss", e.toString());
                                        }
                                        EventBus.getDefault().post("update");
                                    }
                                });
                                threadsearch.start();
                                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

    }

    @Override
    public int getCount() {
        if(list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public ChatItem getItem(int position) {
        if(list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position){
        list.remove(position);
    }
}
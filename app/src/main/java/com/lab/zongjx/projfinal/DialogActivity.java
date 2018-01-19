package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class DialogActivity extends AppCompatActivity {
    private TextView header;
    private Button send;
    private EditText edit;
    private ListView dialog_list;
    private ArrayList<DialogItem> dialogitem;
    private MyDialogAdapter dialogadapter;
    private FloatingActionButton back;
    private Intent intent;
    private Context context;
    private final int REFRESH = 1;
    private final int CLEAR = 2;
    public volatile boolean exit = false;
    private Bitmap from_photo;
    private Bitmap to_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        intent = this.getIntent();
        context = this;

        header = (TextView) findViewById(R.id.name_dialog);
        dialog_list = (ListView) findViewById(R.id.list_dialog);
        dialogitem = new ArrayList<DialogItem>();
        dialogadapter = new MyDialogAdapter(this,dialogitem);
        dialog_list.setAdapter(dialogadapter);
        send = (Button) findViewById(R.id.button_dialog);
        edit = (EditText) findViewById(R.id.edit_dialog);
        back = (FloatingActionButton) findViewById(R.id.back_dialog);

        header = (TextView) findViewById(R.id.name_dialog);
        header.setText(intent.getExtras().getString("to"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit = true;
                finish();
            }
        });

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case REFRESH:{
                        dialog_list.setAdapter(dialogadapter);
                        break;
                    }
                    case CLEAR:{
                        edit.setText("");
                        break;
                    }
                }
            }
        };

        Thread threadinit = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (InterruptedException e){
                    Log.v("ss",e.toString());
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }

                String ip = "120.78.73.208";
                int port = 3306;
                String dbName = "zuazu";
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                String USER = "root";
                String PASSWORD = "123456";

                try{
                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                    Log.v("ss","success");
                    String sql = "select * from dialog where to_who = '" + intent.getExtras().getString("from") + "' and from_who = '" + intent.getExtras().getString("to") + "' or to_who = '" + intent.getExtras().getString("to") + "' and from_who = '" + intent.getExtras().getString("from") + "';";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        if(rs.getString("from_who").equals(intent.getExtras().getString("from"))){
                            dialogitem.add(new DialogItem(rs.getString("dialog"),rs.getString("send_time"),from_photo,true));
                        }
                        else{
                            dialogitem.add(new DialogItem(rs.getString("dialog"),rs.getString("send_time"),to_photo,false));
                        }
                    }
                    dialogadapter = new MyDialogAdapter(context,dialogitem);
                    handler.obtainMessage(REFRESH).sendToTarget();
                    rs.close();
                    st.close();
                    conn.close();
                }catch(SQLException e){
                    Log.v("ss","fail");
                    Log.v("ss",e.getMessage());
                }
            }
        });


        Thread threadgetphoto = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (InterruptedException e){
                    Log.v("ss",e.toString());
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }

                String ip = "120.78.73.208";
                int port = 3306;
                String dbName = "zuazu";
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                String USER = "root";
                String PASSWORD = "123456";

                try{
                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                    Log.v("ss","success");
                    String sql = "select * from user where nickname = '" + intent.getExtras().getString("from") + "';";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        byte [] temp;
                        temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                        from_photo = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                    }
                    sql = "select * from user where nickname = '" + intent.getExtras().getString("to") + "';";
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        byte [] temp;
                        temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                        to_photo = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                    }
                    rs.close();
                    st.close();
                    conn.close();
                    threadinit.start();
                }catch(SQLException e){
                    Log.v("ss","fail");
                    Log.v("ss",e.getMessage());
                }
            }
        });
        threadgetphoto.start();




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit.getText().equals("")){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(!Thread.interrupted()){
                                try{
                                    Thread.sleep(100);
                                    Class.forName("com.mysql.jdbc.Driver");
                                }catch (InterruptedException e){
                                    Log.v("ss",e.toString());
                                }catch (ClassNotFoundException e){
                                    e.printStackTrace();
                                }

                                String ip = "120.78.73.208";
                                int port = 3306;
                                String dbName = "zuazu";
                                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                                String USER = "root";
                                String PASSWORD = "123456";

                                try{
                                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                                    Log.v("ss","success");
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                    String t=format.format(new Date());
                                    Log.e("msg", t);
                                    String sql = "insert into dialog(from_who, to_who, dialog, get,send_time) values('" + intent.getExtras().getString("from") + "','"
                                            + intent.getExtras().getString("to") + "','"
                                            + edit.getText().toString() + "','0','"
                                            + t + "');";
                                    dialogitem.add(new DialogItem(edit.getText().toString(),t,from_photo,true));
                                    dialogadapter = new MyDialogAdapter(context,dialogitem);
                                    handler.obtainMessage(REFRESH).sendToTarget();
                                    handler.obtainMessage(CLEAR).sendToTarget();
                                    Statement st = (Statement) conn.createStatement();
                                    st.executeUpdate(sql);
                                    st.close();
                                    conn.close();
                                    return;
                                }catch(SQLException e){
                                    Log.v("ss","fail");
                                    Log.v("ss",e.getMessage());
                                }
                            }
                        }
                    });
                    thread.start();
                }
                else{
                    Toast.makeText(getApplicationContext(),"发送的内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });



        Thread threadsearch = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){

                    }

                    try{
                        Thread.sleep(100);
                        Class.forName("com.mysql.jdbc.Driver");
                    }catch (InterruptedException e){
                        Log.v("ss",e.toString());
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }

                    String ip = "120.78.73.208";
                    int port = 3306;
                    String dbName = "zuazu";
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                    String USER = "root";
                    String PASSWORD = "123456";


                    try{
                        Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                        Log.v("ss","success");
                        String sql = "select * from dialog where get = '0' and to_who = '" + intent.getExtras().getString("from") + "' and from_who = '" + intent.getExtras().getString("to") + "';";
                        Statement st = (Statement) conn.createStatement();
                        ResultSet rs = st.executeQuery(sql);
                        while(rs.next()){
                            if(rs.getString("from_who").equals(intent.getExtras().getString("from"))){
                                dialogitem.add(new DialogItem(rs.getString("dialog"),rs.getString("send_time"),from_photo,true));
                            }
                            else{
                                dialogitem.add(new DialogItem(rs.getString("dialog"),rs.getString("send_time"),to_photo,false));
                            }
                            dialogadapter = new MyDialogAdapter(context,dialogitem);
                            handler.obtainMessage(REFRESH).sendToTarget();
                            String sqlcheck = "update dialog set get = '1' where id = '" + rs.getInt("id") + "';";
                            st.executeUpdate(sqlcheck);
                        }
                        rs.close();
                        st.close();
                        conn.close();
                    }catch(SQLException e){
                        Log.v("ss","fail");
                        Log.v("ss",e.getMessage());
                    }
                }
            }
        });
        threadsearch.start();
    }

    @Override
    public void onStop(){
        super.onStop();
        exit = true;
    }
}

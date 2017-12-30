package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {
    private Button send;
    private EditText edit;
    private ListView dialog_list;
    private ArrayList<String> dialogitem;
    private MyDialogAdapter dialogadapter;
    private FloatingActionButton back;
    private Intent intent;
    private Context context;
    private final int REFRESH = 1;
    private final int CLEAR = 2;
    public volatile boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        intent = this.getIntent();
        context = this;

        dialog_list = (ListView) findViewById(R.id.list_dialog);
        dialogitem = new ArrayList<String>();
        dialogadapter = new MyDialogAdapter(this,dialogitem);
        dialog_list.setAdapter(dialogadapter);
        send = (Button) findViewById(R.id.button_dialog);
        edit = (EditText) findViewById(R.id.edit_dialog);
        back = (FloatingActionButton) findViewById(R.id.back_dialog);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit = true;
                //finish();
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                String sql = "insert into dialog(from_who, to_who, dialog, get) values('" + intent.getExtras().getString("from") + "','"
                                        + intent.getExtras().getString("to") + "','"
                                        + edit.getText().toString() + "','0');";
                                dialogitem.add(intent.getExtras().getString("from") + ":" + edit.getText().toString());
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
        });



        Thread threadsearch = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try{
                        Thread.sleep(4000);
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
                            dialogitem.add(rs.getString("from_who") + ":" + rs.getString("dialog"));
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
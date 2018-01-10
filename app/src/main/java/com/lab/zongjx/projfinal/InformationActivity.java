package com.lab.zongjx.projfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InformationActivity extends AppCompatActivity {
    private ImageView photo;
    private ImageView back;
    private TextView nickname;
    private TextView realname;
    private TextView sex;
    private TextView campus;
    private TextView phone;
    private Button contact;
    private Button dial;
    private Intent intent;
    private final int SUCCESS = 1;
    private final int SETDATA = 2;

    private String snickname;
    private String srealname;
    private String ssex;
    private String scampus;
    private String sphone;
    private Bitmap sphoto;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        intent = this.getIntent();

        progressBar = (ProgressBar) findViewById(R.id.progress_info);
        constraintLayout = (ConstraintLayout) findViewById(R.id.content_info);
        back = (ImageView) findViewById(R.id.back_info);
        photo = (ImageView) findViewById(R.id.photo_info);
        nickname = (TextView) findViewById(R.id.nickname_info);
        realname = (TextView) findViewById(R.id.realname_info);
        sex = (TextView) findViewById(R.id.sex_info);
        campus = (TextView) findViewById(R.id.campus_info);
        phone = (TextView) findViewById(R.id.phone_info);
        contact = (Button) findViewById(R.id.contact_info);
        dial = (Button) findViewById(R.id.dial_info);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case SUCCESS:{
                        Toast.makeText(getApplicationContext(),"已将对方加入聊天列表",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case SETDATA:{
                        photo.setImageBitmap(sphoto);
                        nickname.setText(snickname);
                        realname.setText(srealname);
                        sex.setText(ssex);
                        campus.setText(scampus);
                        phone.setText(sphone);
                        progressBar.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        };

        if(intent.getExtras().getString("target").equals(intent.getExtras().getString("nickname"))){
            contact.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Thread thread = new Thread(new Runnable() {
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
                    String sql = "select * from user where nickname = '" + intent.getExtras().getString("target") + "';";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    Log.v("target1", intent.getExtras().getString("target"));
                    if(rs.next()) {
                        byte [] temp;
                        Log.v("target", intent.getExtras().getString("target"));
                        temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                        sphoto = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                        snickname = rs.getString("nickname");
                        srealname = rs.getString("realname");
                        ssex = rs.getString("sex");
                        scampus = rs.getString("campus");
                        sphone = rs.getString("phone");
                        handler.obtainMessage(SETDATA).sendToTarget();
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.v("ss", "fail");
                    Log.v("ss", e.getMessage());
                }
            }
        });
        thread.start();

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phone.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread nthread = new Thread(new Runnable() {
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
                            String sql = "select * from chat where from_who = '" + intent.getExtras().getString("nickname") + "' and  to_who = '" + intent.getExtras().getString("target") + "';";
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            if(!rs.next()) {
                                String sql2 = "insert into chat(from_who,to_who) values('" + intent.getExtras().getString("nickname") + "', '" + intent.getExtras().getString("target") + "');";
                                st.executeUpdate(sql2);
                            }
                            sql = "select * from chat where from_who = '" + intent.getExtras().getString("target") + "' and  to_who = '" + intent.getExtras().getString("nickname") + "';";
                            rs = st.executeQuery(sql);
                            if(!rs.next()) {
                                String sql2 = "insert into chat(from_who,to_who) values('" + intent.getExtras().getString("target") + "', '" + intent.getExtras().getString("nickname") + "');";
                                st.executeUpdate(sql2);
                            }
                            handler.obtainMessage(SUCCESS).sendToTarget();
                            rs.close();
                            st.close();
                            conn.close();
                        } catch (SQLException e) {
                            Log.v("ss", "fail");
                            Log.v("ss", e.getMessage());
                        }
                    }
                });
                nthread.start();
            }
        });

    }
}

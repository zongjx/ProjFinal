package com.lab.zongjx.projfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForgetActivity extends AppCompatActivity {
    private final int NOT_EXIST = 1;
    private final int NOT_MATCH = 2;
    private EditText account;
    private EditText campus;
    private EditText studentid;
    private Button back;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        account = (EditText) findViewById(R.id.account_forget);
        campus = (EditText) findViewById(R.id.campus_forget);
        studentid = (EditText) findViewById(R.id.studentid_forget);
        back = (Button) findViewById(R.id.back_forget);
        submit = (Button) findViewById(R.id.submit_forget);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ForgetActivity.this);
                alertDialog.setTitle("返回")
                        .setMessage("确认要离开这个页面吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
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

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case NOT_MATCH:{
                        Toast.makeText(getApplicationContext(),"校验信息不匹配！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NOT_EXIST:{
                        Toast.makeText(getApplicationContext(),"账号不存在！",Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"账号不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(campus.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学校不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(studentid.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学号不能为空！",Toast.LENGTH_SHORT).show();
                }
                else{
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
                                    String sql = "select * from user where account = '" + account.getText().toString() + "';";
                                    Statement st = (Statement) conn.createStatement();
                                    ResultSet rs = st.executeQuery(sql);
                                    if(rs.next()){
                                        Log.v("name","success");
                                        if(rs.getString("campus").equals(campus.getText().toString()) && rs.getString("studentid").equals(studentid.getText().toString())){
                                            Intent intent = new Intent(ForgetActivity.this,NewPasswordActivity.class);
                                            Bundle extras = new Bundle();
                                            extras.putInt("id",rs.getInt("id"));
                                            extras.putString("account",rs.getString("account"));
                                            intent.putExtras(extras);
                                            rs.close();
                                            st.close();
                                            conn.close();
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            handler.obtainMessage(NOT_MATCH).sendToTarget();
                                        }
                                    }
                                    else{
                                        handler.obtainMessage(NOT_EXIST).sendToTarget();
                                    }
                                    rs.close();
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
            }
        });

    }

}

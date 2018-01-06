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
import java.sql.SQLException;
import java.sql.Statement;

public class NewPasswordActivity extends AppCompatActivity {
    private final int MODIFY_SUCCESS = 1;
    private EditText newpassword;
    private EditText confirm_password;
    private Button back;
    private Button submit;
    private Intent intent;
    private Bundle extars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword);
        intent = this.getIntent();
        extars = intent.getExtras();

        newpassword = (EditText) findViewById(R.id.newpassword_newpassword);
        confirm_password = (EditText) findViewById(R.id.confirm_password_newpassword);
        back = (Button) findViewById(R.id.back_newpassword);
        submit = (Button) findViewById(R.id.submit_newpassword);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewPasswordActivity.this);
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
                    case MODIFY_SUCCESS:{
                        Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"新密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"确认密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!newpassword.getText().toString().equals(confirm_password.getText().toString())){
                        Toast.makeText(getApplicationContext(),"两次输入的密码不一致！",Toast.LENGTH_SHORT).show();
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
                                        String sql = "update user set password = '"
                                                + MD5Utils.encode(newpassword.getText().toString())
                                                + "' where account = '" + extars.getString("account") + "';";
                                        Statement st = (Statement) conn.createStatement();
                                        st.executeUpdate(sql);
                                        handler.obtainMessage(MODIFY_SUCCESS).sendToTarget();
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
            }
        });
    }

}

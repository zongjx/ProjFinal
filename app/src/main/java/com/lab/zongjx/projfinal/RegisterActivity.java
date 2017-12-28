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
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {
    private final int ACCOUNT_EXIST = 1;
    private final int REGISTER_SUCCESS = 2;
    private EditText account;
    private EditText password;
    private EditText confirm_password;
    private EditText nickname;
    private EditText realname;
    private EditText campus;
    private EditText studentid;
    private RadioGroup sex;
    private Button back;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (EditText) findViewById(R.id.account_register);
        password = (EditText) findViewById(R.id.password_register);
        confirm_password = (EditText) findViewById(R.id.confirm_password_register);
        nickname = (EditText) findViewById(R.id.nickname_register);
        realname = (EditText) findViewById(R.id.realname_register);
        campus = (EditText) findViewById(R.id.campus_register);
        studentid = (EditText) findViewById(R.id.studentid_register);
        sex = (RadioGroup) findViewById(R.id.sex_register);
        back = (Button) findViewById(R.id.back_register);
        submit = (Button) findViewById(R.id.submit_register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
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
                    case ACCOUNT_EXIST:{
                        Toast.makeText(getApplicationContext(),"此账号已经存在，请注册其他账号！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case REGISTER_SUCCESS:{
                        Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
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
                else if(password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"确认密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(nickname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"昵称不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(realname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"姓名不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(sex.getCheckedRadioButtonId() != R.id.sex_male && sex.getCheckedRadioButtonId() != R.id.sex_female && sex.getCheckedRadioButtonId() != R.id.sex_secret){
                    Toast.makeText(getApplicationContext(),"性别不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(campus.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学校不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(studentid.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学号不能为空！",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!password.getText().toString().equals(confirm_password.getText().toString())){
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
                                    String dbName = "android";
                                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
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
                                            handler.obtainMessage(ACCOUNT_EXIST).sendToTarget();
                                        }
                                        else{
                                            String tempsex;
                                            if(sex.getCheckedRadioButtonId() == R.id.sex_male){
                                                tempsex = "男";
                                            }
                                            else if(sex.getCheckedRadioButtonId() == R.id.sex_female){
                                                tempsex = "女";
                                            }
                                            else{
                                                tempsex = "保密";
                                            }
                                            sql = "insert into user(account, password, nickname, realname, sex, campus, studentid) values('"
                                                    + account.getText().toString() + "'"
                                                    + password.getText().toString() + "'"
                                                    + nickname.getText().toString() + "'"
                                                    + realname.getText().toString() + "'"
                                                    + tempsex + "'"
                                                    + campus.getText().toString() + "'"
                                                    + studentid.getText().toString() + "');";
                                            st.executeUpdate(sql);
                                            handler.obtainMessage(ACCOUNT_EXIST).sendToTarget();
                                            rs.close();
                                            st.close();
                                            conn.close();
                                            finish();
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

                    }

                }

            }
        });


    }
}

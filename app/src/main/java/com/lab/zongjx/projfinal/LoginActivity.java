package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tslamic.fancybg.FancyBackground;

public class LoginActivity extends AppCompatActivity {
    private final int WRONG_PASSWORD = 1;
    private final int NOT_EXIST = 2;
    private final int LOADING= 3;
    private final int FINISH = 4;
    private final int SETLOGO = 5;
    private TextInputLayout account_layout;
    private TextInputLayout password_layout;
    private Button login;
    private ProgressBar bar;
    private TextView forget;
    private TextView register;
    private EditText account;
    private EditText password;
    private ConstraintLayout constraintLayout;
    private ImageView logo;
    private Bitmap photo;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = (ImageView) findViewById(R.id.logo_login);
        account_layout = (TextInputLayout) findViewById(R.id.textInputLayout_account_login);
        password_layout = (TextInputLayout) findViewById(R.id.textInputLayout_password_login);
        forget = (TextView) findViewById(R.id.forget_login);
        login = (Button) findViewById(R.id.login_login);
        register = (TextView) findViewById(R.id.register_login);
        account = account_layout.getEditText();
        password = password_layout.getEditText();
        constraintLayout = (ConstraintLayout) findViewById(R.id.parent_login);
        view = (View) findViewById(R.id.parent_login);
        bar = (ProgressBar) findViewById(R.id.loading_login);
        bar.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case WRONG_PASSWORD:{
                        Toast.makeText(getApplicationContext(),"账号或密码错误！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NOT_EXIST:{
                        Toast.makeText(getApplicationContext(),"账号不存在！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case LOADING:{
                        bar.setVisibility(View.VISIBLE);
                        login.setVisibility(View.GONE);
                        break;
                    }
                    case FINISH:{
                        bar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        break;
                    }
                    case SETLOGO:{
                        logo.setImageBitmap(photo);
                        break;
                    }

                }
            }
        };

        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
                            String sql = "select * from user where account = '" + account.getText().toString() + "';";
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);

                            while (rs.next()) {
                                byte [] temp;
                                temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                                photo = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                                handler.obtainMessage(SETLOGO).sendToTarget();
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
                threadsearch.start();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString().isEmpty()){
                    account_layout.setErrorEnabled(true);
                    account_layout.setError("账号不能为空哦！");
                }
                else if(password.getText().toString().isEmpty()){
                    password_layout.setErrorEnabled(true);
                    password_layout.setError("密码不能为空哦！");
                }
                else{
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.obtainMessage(LOADING).sendToTarget();
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
                                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName +
                                        "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
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
                                        if(rs.getString("password").equals(MD5Utils.encode(password.getText().toString()))){
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            Bundle extras = new Bundle();
                                            extras.putInt("id",rs.getInt("id"));
                                            extras.putString("account",rs.getString("account"));
                                            extras.putString("nickname",rs.getString("nickname"));
                                            extras.putString("photo",rs.getString("photo"));
                                            intent.putExtras(extras);
                                            rs.close();
                                            st.close();
                                            conn.close();
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            handler.obtainMessage(WRONG_PASSWORD).sendToTarget();
                                        }
                                    }
                                    else{
                                        handler.obtainMessage(NOT_EXIST).sendToTarget();
                                    }
                                    handler.obtainMessage(FINISH).sendToTarget();
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

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                account_layout.setErrorEnabled(false);
                account_layout.setError("");
                password_layout.setErrorEnabled(false);
                password_layout.setError("");
                constraintLayout.setFocusable(true);
                constraintLayout.setFocusableInTouchMode(true);
                constraintLayout.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
                return false;
            }
        });
    }
}

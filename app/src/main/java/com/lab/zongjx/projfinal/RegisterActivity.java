package com.lab.zongjx.projfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {
    private final int ACCOUNT_EXIST = 1;
    private final int REGISTER_SUCCESS = 2;
    private final int NICKNAME_EXIST = 3;
    private final int SHOW = 4;
    private ImageView photo;
    private EditText account;
    private EditText password;
    private EditText confirm_password;
    private EditText nickname;
    private EditText realname;
    private EditText campus;
    private EditText studentid;
    private EditText phone;
    private EditText question;
    private EditText answer;
    private RadioGroup sex;
    private Button back;
    private Button submit;
    private ProgressBar progressBar;
    private Uri imageUri;
    public static final int CROP_PHOTO = 2;
    private byte[] in;
    private String store;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        photo = (ImageView) findViewById(R.id.photo_register);
        account = (EditText) findViewById(R.id.account_register);
        password = (EditText) findViewById(R.id.password_register);
        confirm_password = (EditText) findViewById(R.id.confirm_password_register);
        nickname = (EditText) findViewById(R.id.nickname_register);
        realname = (EditText) findViewById(R.id.realname_register);
        campus = (EditText) findViewById(R.id.campus_register);
        studentid = (EditText) findViewById(R.id.studentid_register);
        sex = (RadioGroup) findViewById(R.id.sex_register);
        phone = (EditText) findViewById(R.id.phone_register);
        question = (EditText) findViewById(R.id.question_register);
        answer = (EditText) findViewById(R.id.answer_register);
        back = (Button) findViewById(R.id.back_register);
        submit = (Button) findViewById(R.id.submit_register);
        progressBar = (ProgressBar) findViewById(R.id.progress_register);

        Resources res = RegisterActivity.this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.account);
        Bitmap bm = Bitmap.createScaledBitmap(bmp,100,100,true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        in = baos.toByteArray();
        store = Base64.encodeToString(in, Base64.DEFAULT);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.
                        getExternalStorageDirectory(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,CROP_PHOTO);
            }
        });

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
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"此账号已经存在，请注册其他账号！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case NICKNAME_EXIST:{
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"此昵称已经存在，请注册其他昵称！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case REGISTER_SUCCESS:{
                        progressBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case SHOW:{
                        progressBar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);
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
                else if(sex.getCheckedRadioButtonId() != R.id.sex_male && sex.getCheckedRadioButtonId() != R.id.sex_female
                        && sex.getCheckedRadioButtonId() != R.id.sex_secret){
                    Toast.makeText(getApplicationContext(),"性别不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(campus.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学校不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(studentid.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"学号不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(phone.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"电话不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(question.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"保密问题不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(answer.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"答案不能为空！",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!password.getText().toString().equals(confirm_password.getText().toString())){
                        Toast.makeText(getApplicationContext(),"两次输入的密码不一致！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.obtainMessage(SHOW).sendToTarget();
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
                                        String sql = "select * from user where account = '" + account.getText().toString()
                                                + "' or nickname = '" + nickname.getText().toString()  + "';";
                                        Statement st = (Statement) conn.createStatement();
                                        ResultSet rs = st.executeQuery(sql);
                                        if(rs.next()){
                                            Log.v("name","success");
                                            if(rs.getString("account").equals(account.getText().toString())){
                                                handler.obtainMessage(ACCOUNT_EXIST).sendToTarget();
                                            }
                                            else if(rs.getString("nickname").equals(nickname.getText().toString())){
                                                handler.obtainMessage(NICKNAME_EXIST).sendToTarget();
                                            }
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
                                            sql = "insert into user(account, password, nickname, realname, sex, campus, studentid, phone, photo, question, answer) values('"
                                                    + account.getText().toString() + "','"
                                                    + MD5Utils.encode(password.getText().toString())  + "','"
                                                    + nickname.getText().toString() + "','"
                                                    + realname.getText().toString() + "','"
                                                    + tempsex + "','"
                                                    + campus.getText().toString() + "','"
                                                    + studentid.getText().toString() + "','"
                                                    + phone.getText().toString() + "','"
                                                    + store + "','"
                                                    + question.getText().toString() + "','"
                                                    + MD5Utils.encode(answer.getText().toString()) + "');";
                                            st.executeUpdate(sql);
                                            handler.obtainMessage(REGISTER_SUCCESS).sendToTarget();
                                            rs.close();
                                            st.close();
                                            conn.close();
                                            finish();
                                        }
                                        rs.close();
                                        st.close();
                                        conn.close();
                                    }catch(SQLException e){
                                        Log.v("ss","fail");
                                        Log.v("ss",e.getMessage());
                                    }
                            }
                        });
                        thread.start();
                    }

                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CROP_PHOTO){
            if (resultCode == RESULT_OK) {
                try {
                    if(data != null){
                        imageUri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap bm = Bitmap.createScaledBitmap(bitmap,100,100,true);
                        photo.setImageBitmap(bm); // 将裁剪后的照片显示出来
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        in = baos.toByteArray();
                        store = Base64.encodeToString(in, Base64.DEFAULT);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

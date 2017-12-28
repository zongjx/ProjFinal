package com.lab.zongjx.projfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private EditText confirm_password;
    private EditText nickname;
    private EditText realname;
    private EditText campus;
    private EditText studentid;
    private RadioGroup sex;

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



    }
}

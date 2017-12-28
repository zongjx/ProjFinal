package com.lab.zongjx.projfinal;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tslamic.fancybg.FancyBackground;

public class LoginActivity extends AppCompatActivity implements FancyBackground.FancyListener{
    private static final String TAG = "FancyBackground";
    private TextInputLayout account_layout;
    private TextInputLayout password_layout;
    private Button login;
    private TextView forget;
    private TextView register;
    private EditText account;
    private EditText password;
    private ConstraintLayout constraintLayout;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account_layout = (TextInputLayout) findViewById(R.id.textInputLayout_account_login);
        password_layout = (TextInputLayout) findViewById(R.id.textInputLayout_password_login);
        forget = (TextView) findViewById(R.id.forget_login);
        login = (Button) findViewById(R.id.login_login);
        register = (TextView) findViewById(R.id.register_login);
        account = account_layout.getEditText();
        password = password_layout.getEditText();
        constraintLayout = (ConstraintLayout) findViewById(R.id.parent_login);
        view = (View) findViewById(R.id.parent_login);

        FancyBackground.on(view)
                .set(R.drawable.fbg_fst, R.drawable.fbg_snd, R.drawable.fbg_trd)
                .inAnimation(R.anim.fade_in)
                .outAnimation(R.anim.fade_out)
                .interval(4000)
                .start();

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

                }
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

    @Override
    public void onStarted(FancyBackground bg) {
        Log.d(TAG, "Started FancyBackground.");
    }

    @Override
    public void onNew(FancyBackground bg) {
        Log.d(TAG, "New pic loaded.");
    }

    @Override
    public void onStopped(FancyBackground bg) {
        Log.d(TAG, "Stopped FancyBackground.");
    }

    @Override
    public void onLoopDone(FancyBackground bg) {
        Log.d(TAG, "Loop complete.");
    }

}

package com.lab.zongjx.projfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.*;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.w3c.dom.ls.LSException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FlowingDrawer mDrawer;
    private TextView nickname;
    private ListView menu;
    private ArrayList<String> choice;
    private MyMenuAdapter choice_adapter;
    private Intent intent;
    private FrameLayout homepage = (FrameLayout) findViewById(R.id.home_main);
    private FrameLayout chatpage = (FrameLayout) findViewById(R.id.chat_main);
    private FrameLayout teampage = (FrameLayout) findViewById(R.id.team_main);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:{
                    homepage.setVisibility(View.VISIBLE);
                    chatpage.setVisibility(View.GONE);
                    teampage.setVisibility(View.GONE);
                    return true;
                }
                case R.id.navigation_chat:{
                    homepage.setVisibility(View.GONE);
                    chatpage.setVisibility(View.VISIBLE);
                    teampage.setVisibility(View.GONE);
                    return true;
                }
                case R.id.navigation_team:{
                    homepage.setVisibility(View.GONE);
                    chatpage.setVisibility(View.GONE);
                    teampage.setVisibility(View.VISIBLE);
                    return true;
                }
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = this.getIntent();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        nickname = (TextView) findViewById(R.id.nickname_main);
        menu = (ListView) findViewById(R.id.menu_main);
        choice = new ArrayList<String>(){{
            add("修改信息");
            add("修改密码");
            add("注销账号");
        }};
        choice_adapter = new MyMenuAdapter(this,choice) {
        };
        menu.setAdapter(choice_adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{

                        break;
                    }
                    case 1:{
                        Intent nintent = new Intent(MainActivity.this,NewPasswordActivity.class);
                        Bundle nexteas = new Bundle();
                        nexteas.putString("account",intent.getExtras().getString("account"));
                        intent.putExtras(nexteas);
                        startActivity(nintent);
                        break;
                    }
                    case 2:{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("返回")
                                .setMessage("确认要退出登录吗？")
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
                        break;
                    }
                }
            }
        });

        String temps = "您好！  " + intent.getExtras().getString("nickname");
        nickname.setText(temps);





    }

}

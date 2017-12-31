package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.*;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.w3c.dom.ls.LSException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final int CHATREFRESH = 1;
    private final int MAINREFRESH = 2;
    private FlowingDrawer mDrawer;
    private TextView nickname;
    private ListView menu;
    private ArrayList<String> choice;
    private MyMenuAdapter choice_adapter;
    private Intent intent;
    private FrameLayout homepage;
    private FrameLayout chatpage;
    private FrameLayout teampage;
    private PullRefreshLayout layout;
    private MyMainAdapter adapter;

    protected FloatingActionButton FAB;
    private RecyclerView mRecyclerView;
    private ArrayList<String> titleSet = new ArrayList();
    private ArrayList<String> ddlSet = new ArrayList();
    private ArrayList<String> numSet = new ArrayList();
    private ArrayList<String> teammaresSet = new ArrayList();
    private ArrayList<String> contentSet = new ArrayList();
    private ArrayList<String> idSet = new ArrayList();

    private ListView chatlist;
    private ArrayList<String> chatitem;
    private MyChatAdapter chatadapter;
    public static Context maincontext;



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
        maincontext = this;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homepage = (FrameLayout) findViewById(R.id.home_main);
        chatpage = (FrameLayout) findViewById(R.id.chat_main);
        teampage = (FrameLayout) findViewById(R.id.team_main);

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

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CHATREFRESH:{
                        chatlist.setAdapter(chatadapter);
                        break;
                    }
                    case MAINREFRESH: {
                        adapter = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet, titleSet, ddlSet, numSet, teammaresSet, contentSet,maincontext);
                        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(maincontext));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                        layout.setRefreshing(false);
                        break;
                    }
                }
            }
        };

        nickname = (TextView) findViewById(R.id.nickname_main);
        menu = (ListView) findViewById(R.id.menu_main);
        choice = new ArrayList<String>(){{
            add("个人信息");
            add("修改密码");
            add("搜索用户");
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
                    case 3:{
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ButterKnife.bind(this);

        FAB = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bintent = new Intent(MainActivity.this,EditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accountName",intent.getExtras().getString("nickname"));
                bintent.putExtras(bundle);
                startActivityForResult(bintent,0);
            }
        });


        Thread refreshthread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
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

                idSet.clear();
                titleSet.clear();
                ddlSet.clear();
                numSet.clear();
                teammaresSet.clear();
                contentSet.clear();

                String ip = "120.78.73.208";
                int port = 3306;
                String dbName = "zuazu";
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                String USER = "root";
                String PASSWORD = "123456";

                try{
                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                    Log.v("ss","success");
                    String sql = "select * from msg ;";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        idSet.add(rs.getString("msgid"));
                        titleSet.add(rs.getString("title"));
                        ddlSet.add(rs.getString("ddl"));
                        numSet.add(rs.getString("num"));
                        teammaresSet.add(rs.getString("teammates"));
                        contentSet.add(rs.getString("content"));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                }catch(SQLException e){
                    Log.v("ss","fail");
                    Log.v("ss",e.getMessage());
                }
                handler.obtainMessage(MAINREFRESH).sendToTarget();
            }
        });
        refreshthread.start();


        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            Class.forName("com.mysql.jdbc.Driver");
                        }catch (ClassNotFoundException e){
                            e.printStackTrace();
                        }

                        idSet.clear();
                        titleSet.clear();
                        ddlSet.clear();
                        numSet.clear();
                        teammaresSet.clear();
                        contentSet.clear();

                        String ip = "120.78.73.208";
                        int port = 3306;
                        String dbName = "zuazu";
                        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                        String USER = "root";
                        String PASSWORD = "123456";

                        try{
                            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                            Log.v("ss","success111");
                            String sql = "select * from msg ;";
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            while(rs.next()){
                                idSet.add(rs.getString("msgid"));
                                titleSet.add(rs.getString("title"));
                                ddlSet.add(rs.getString("ddl"));
                                numSet.add(rs.getString("num"));
                                teammaresSet.add(rs.getString("teammates"));
                                contentSet.add(rs.getString("content"));
                            }
                            rs.close();
                            st.close();
                            conn.close();
                            handler.obtainMessage(MAINREFRESH).sendToTarget();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                thread.start();
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);








/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        chatlist = (ListView) findViewById(R.id.chatlist_chat);
        chatitem = new ArrayList<String>(){{
        }};
        chatadapter = new MyChatAdapter(this,chatitem) {
        };
        chatlist.setAdapter(chatadapter);
        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nintent = new Intent(MainActivity.this,DialogActivity.class);
                Bundle nextras = new Bundle();
                nextras.putString("from",intent.getExtras().getString("nickname"));
                nextras.putString("to",chatadapter.getItem(position));
                nintent.putExtras(nextras);
                startActivity(nintent);
            }
        });



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
                        String sql = "select * from chat where from_who = '" + intent.getExtras().getString("nickname") + "';";
                        Statement st = (Statement) conn.createStatement();
                        ResultSet rs = st.executeQuery(sql);
                        while (rs.next()) {
                            chatitem.add(rs.getString("to_who"));
                        }
                        handler.obtainMessage(CHATREFRESH).sendToTarget();
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
}

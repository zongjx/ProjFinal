package com.lab.zongjx.projfinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.ls.LSException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private long firstTime;
    private final int CHATREFRESH = 1;
    private final int MAINREFRESH = 2;
    private final int START = 3;
    private final int MAINREFRESH2 = 4;
    private final int START2 = 5;
    private FlowingDrawer mDrawer;
    private TextView nickname;
    private ListView menu;
    private ImageView photo;
    private ArrayList<String> choice;
    private MyMenuAdapter choice_adapter;
    private Intent intent;
    private FrameLayout homepage;
    private FrameLayout chatpage;
    private FrameLayout teampage;
    private PullRefreshLayout layout2;

    private PullRefreshLayout layout,layout3;
    private MyMainAdapter adapter,adapter2;
    protected FloatingActionButton FAB;
    protected SearchView searchView;
    private MyrecyclerView mRecyclerView,mRecyclerView2;
    private ArrayList<String> publisherSet = new ArrayList(),publisherSet1 = new ArrayList();
    private ArrayList<String> titleSet = new ArrayList(),titleSet1 = new ArrayList();
    private ArrayList<String> ddlSet = new ArrayList(),ddlSet1 = new ArrayList();
    private ArrayList<String> numSet = new ArrayList(),numSet1 = new ArrayList();
    private ArrayList< ArrayList<String> > teammaresSet = new ArrayList(),teammaresSet1 = new ArrayList();
    private ArrayList<String> contentSet = new ArrayList(),contentSet1 = new ArrayList();
    private ArrayList<String> idSet = new ArrayList(),idSet1 = new ArrayList();

    private ListView chatlist;
    private ArrayList<ChatItem> chatitem;
    private MyChatAdapter chatadapter;
    public static Context maincontext;

    private Thread threadsearch;



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

        EventBus.getDefault().register(this);
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

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CHATREFRESH:{
                        chatadapter = new MyChatAdapter(MainActivity.this,chatitem) {
                        };
                        chatlist.setAdapter(chatadapter);
                        layout2.setRefreshing(false);
                        break;
                    }
                    case START: {
                        adapter = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet, titleSet, ddlSet, numSet, teammaresSet, contentSet, publisherSet, maincontext);
                        mRecyclerView = (MyrecyclerView)findViewById(R.id.recycler_view);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(maincontext));
                        mRecyclerView.addItemDecoration(new MyrecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                        break;
                    }
                    case MAINREFRESH: {
                        adapter = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet, titleSet, ddlSet, numSet, teammaresSet, contentSet, publisherSet, maincontext);
                        mRecyclerView = (MyrecyclerView)findViewById(R.id.recycler_view);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(maincontext));
                        mRecyclerView.addItemDecoration(new MyrecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin1);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                        layout.setRefreshing(false);
                        break;
                    }
                    case START2: {
                        adapter2 = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet1, titleSet1, ddlSet1, numSet1, teammaresSet1, contentSet1, publisherSet1, maincontext);
                        mRecyclerView2 = (MyrecyclerView)findViewById(R.id.recycler_view2);
                        mRecyclerView2.setLayoutManager(new LinearLayoutManager(maincontext));
                        mRecyclerView2.addItemDecoration(new MyrecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
                        mRecyclerView2.setAdapter(adapter2);
                        break;
                    }
                    case MAINREFRESH2: {
                        adapter2 = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet1, titleSet1, ddlSet1, numSet1, teammaresSet1, contentSet1, publisherSet1, maincontext);
                        mRecyclerView2 = (MyrecyclerView)findViewById(R.id.recycler_view2);
                        mRecyclerView2.setLayoutManager(new LinearLayoutManager(maincontext));
                        mRecyclerView2.addItemDecoration(new MyrecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin1);
                            }
                        });
                        mRecyclerView2.setAdapter(adapter2);
                        layout3.setRefreshing(false);
                        break;
                    }
                }
            }
        };

        nickname = (TextView) findViewById(R.id.nickname_main);
        photo = (ImageView) findViewById(R.id.photo_main);
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
                        Intent nintent  = new Intent(MainActivity.this,InformationActivity.class);
                        Bundle nextras = new Bundle();
                        nextras.putString("nickname",intent.getExtras().getString("nickname"));
                        nextras.putString("target",intent.getExtras().getString("nickname"));
                        nextras.putString("account",intent.getExtras().getString("account"));
                        nintent.putExtras(nextras);
                        startActivity(nintent);
                        break;
                    }
                    case 1:{
                        Intent nintent = new Intent(MainActivity.this,NewPasswordActivity.class);
                        Bundle nexteas = new Bundle();
                        nexteas.putString("account",intent.getExtras().getString("account"));
                        nintent.putExtras(nexteas);
                        startActivity(nintent);
                        break;
                    }
                    case 2:{
                        Intent nintent = new Intent(MainActivity.this,SearchuserActivity.class);
                        Bundle nexteas = new Bundle();
                        nexteas.putString("nickname",intent.getExtras().getString("nickname"));
                        nexteas.putString("account",intent.getExtras().getString("account"));
                        nintent.putExtras(nexteas);
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
                                        Intent nintent = new Intent(MainActivity.this,LoginActivity.class);
                                        startActivity(nintent);
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
                mDrawer.closeMenu();
            }
        });

        String temps = "您好！  " + intent.getExtras().getString("nickname");
        nickname.setText(temps);
        byte[] bitmapArray;
        bitmapArray = Base64.decode(intent.getExtras().getString("photo"), Base64.DEFAULT);
        photo.setImageBitmap(BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length));

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ButterKnife.bind(this);

        searchView = (SearchView)findViewById(R.id.search_main);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入关键字");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                        Intent mintent = new Intent(MainActivity.this,SearchResult.class);
                        Bundle extras = new Bundle();
                        extras.putString("key",searchView.getQuery().toString());
                        extras.putString("nickname",intent.getExtras().getString("nickname"));
                        mintent.putExtras(extras);
                        startActivityForResult(mintent,0);
                    }
                    searchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }
        });

        FAB = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bintent = new Intent(MainActivity.this,EditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accountName",intent.getExtras().getString("nickname"));
                bintent.putExtras(bundle);
                startActivityForResult(bintent,1);
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
                publisherSet.clear();
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
                    String sql = "select * from msg order by msgid desc;";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        idSet.add(rs.getString("msgid"));
                        publisherSet.add(rs.getString("publisher"));
                        titleSet.add(rs.getString("title"));
                        ddlSet.add(rs.getString("ddl"));
                        numSet.add(rs.getString("num"));
                        contentSet.add(rs.getString("content"));
                    }

                    for (int i=0;i<idSet.size();i++){
                        ArrayList<String> tmp = new ArrayList();
                        sql = String.format("select * from teammates where msgid = %s;",idSet.get(i));
                        rs = st.executeQuery(sql);
                        tmp.add(publisherSet.get(i));
                        while(rs.next()){
                            tmp.add(rs.getString("name"));
                        }
                        teammaresSet.add(tmp);
                    }

                    rs.close();
                    st.close();
                    conn.close();
                }catch(SQLException e){
                    Log.v("ss","fail");
                    Log.v("ss",e.getMessage());
                }
                handler.obtainMessage(START).sendToTarget();
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
                        publisherSet.clear();
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
                            String sql = "select * from msg order by msgid desc;";
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            while(rs.next()){
                                idSet.add(rs.getString("msgid"));
                                publisherSet.add(rs.getString("publisher"));
                                titleSet.add(rs.getString("title"));
                                ddlSet.add(rs.getString("ddl"));
                                numSet.add(rs.getString("num"));
                                contentSet.add(rs.getString("content"));
                            }

                            for (int i=0;i<idSet.size();i++){
                                ArrayList<String> tmp = new ArrayList();
                                sql = String.format("select * from teammates where msgid = %s;",idSet.get(i));
                                rs = st.executeQuery(sql);
                                tmp.add(publisherSet.get(i));
                                while(rs.next()){
                                    tmp.add(rs.getString("name"));
                                }
                                teammaresSet.add(tmp);
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
        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





        layout2 = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout2);
        layout2.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                threadsearch.start();
            }
        });
        layout2.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);

        chatlist = (ListView) findViewById(R.id.chatlist_chat);
        chatitem = new ArrayList<ChatItem>(){{
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
                nextras.putString("to",chatadapter.getItem(position).getName());
                nintent.putExtras(nextras);
                startActivity(nintent);
            }
        });

        threadsearch = new Thread(new Runnable() {
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
                    String sql = "select * from user where nickname = any(select to_who from chat where from_who = '" + intent.getExtras().getString("nickname") + "');";
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    chatitem = new ArrayList<ChatItem>(){{
                    }};
                    while (rs.next()) {
                        byte [] temp;
                        temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                        chatitem.add(new ChatItem(BitmapFactory.decodeByteArray(temp, 0, temp.length),rs.getString("nickname"),intent.getExtras().getString("nickname")));
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




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        Thread refreshthread1 = new Thread(new Runnable() {
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

                idSet1.clear();
                publisherSet1.clear();
                titleSet1.clear();
                ddlSet1.clear();
                numSet1.clear();
                teammaresSet1.clear();
                contentSet1.clear();

                String ip = "120.78.73.208";
                int port = 3306;
                String dbName = "zuazu";
                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                String USER = "root";
                String PASSWORD = "123456";

                try{
                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                    Log.v("ss","success");
                    String sql = String.format("select * from msg where publisher = '%s' order by msgid desc;",intent.getExtras().getString("nickname"));
                    Statement st = (Statement) conn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        idSet1.add(rs.getString("msgid"));
                        publisherSet1.add(rs.getString("publisher"));
                        titleSet1.add(rs.getString("title"));
                        ddlSet1.add(rs.getString("ddl"));
                        numSet1.add(rs.getString("num"));
                        contentSet1.add(rs.getString("content"));
                    }


                    ArrayList<String> tmp_idSet = new ArrayList();
                    sql = String.format("select * from teammates where name = '%s' order by msgid desc;",intent.getExtras().getString("nickname"));
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        tmp_idSet.add(rs.getString("msgid"));
                    }
                    for (int i=0;i<tmp_idSet.size();i++){
                        sql = String.format("select * from msg where msgid = %s;",tmp_idSet.get(i));
                        rs = st.executeQuery(sql);
                        while(rs.next()){
                            idSet1.add(rs.getString("msgid"));
                            publisherSet1.add(rs.getString("publisher"));
                            titleSet1.add(rs.getString("title"));
                            ddlSet1.add(rs.getString("ddl"));
                            numSet1.add(rs.getString("num"));
                            contentSet1.add(rs.getString("content"));
                        }
                    }


                    for (int i=0;i<idSet1.size();i++){
                        ArrayList<String> tmp = new ArrayList();
                        sql = String.format("select * from teammates where msgid = %s;",idSet1.get(i));
                        rs = st.executeQuery(sql);
                        tmp.add(publisherSet1.get(i));
                        while(rs.next()){
                            tmp.add(rs.getString("name"));
                        }
                        teammaresSet1.add(tmp);
                    }

                    rs.close();
                    st.close();
                    conn.close();
                }catch(SQLException e){
                    Log.v("ss","fail");
                    Log.v("ss",e.getMessage());
                }
                handler.obtainMessage(START2).sendToTarget();
            }
        });
        refreshthread1.start();


        layout3 = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout3);
        layout3.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
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

                        idSet1.clear();
                        publisherSet1.clear();
                        titleSet1.clear();
                        ddlSet1.clear();
                        numSet1.clear();
                        teammaresSet1.clear();
                        contentSet1.clear();

                        String ip = "120.78.73.208";
                        int port = 3306;
                        String dbName = "zuazu";
                        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                        String USER = "root";
                        String PASSWORD = "123456";

                        try{
                            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                            Log.v("ss","success111");
                            String sql = String.format("select * from msg where publisher = '%s' order by msgid desc;",intent.getExtras().getString("nickname"));
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            while(rs.next()){
                                idSet1.add(rs.getString("msgid"));
                                publisherSet1.add(rs.getString("publisher"));
                                titleSet1.add(rs.getString("title"));
                                ddlSet1.add(rs.getString("ddl"));
                                numSet1.add(rs.getString("num"));
                                contentSet1.add(rs.getString("content"));
                            }


                            ArrayList<String> tmp_idSet = new ArrayList();
                            sql = String.format("select * from teammates where name = '%s' order by msgid desc;",intent.getExtras().getString("nickname"));
                            rs = st.executeQuery(sql);
                            while(rs.next()){
                                tmp_idSet.add(rs.getString("msgid"));
                            }
                            for (int i=0;i<tmp_idSet.size();i++){
                                sql = String.format("select * from msg where msgid = %s;",tmp_idSet.get(i));
                                rs = st.executeQuery(sql);
                                while(rs.next()){
                                    idSet1.add(rs.getString("msgid"));
                                    publisherSet1.add(rs.getString("publisher"));
                                    titleSet1.add(rs.getString("title"));
                                    ddlSet1.add(rs.getString("ddl"));
                                    numSet1.add(rs.getString("num"));
                                    contentSet1.add(rs.getString("content"));
                                }
                            }


                            for (int i=0;i<idSet1.size();i++){
                                ArrayList<String> tmp = new ArrayList();
                                sql = String.format("select * from teammates where msgid = %s;",idSet1.get(i));
                                rs = st.executeQuery(sql);
                                tmp.add(publisherSet1.get(i));
                                while(rs.next()){
                                    tmp.add(rs.getString("name"));
                                }
                                teammaresSet1.add(tmp);
                            }

                            rs.close();
                            st.close();
                            conn.close();
                            handler.obtainMessage(MAINREFRESH2).sendToTarget();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                thread.start();
            }
        });
        layout3.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String s){
        threadsearch.start();
    }

}


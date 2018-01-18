package com.lab.zongjx.projfinal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchuserActivity extends AppCompatActivity {
    private SearchView searchView;
    private ArrayList<ChatItem> useritem;
    private MySearchAdapter useradapter;
    private ListView userlist;
    private final int CHATREFRESH = 1;
    private Intent intent;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);
        intent = this.getIntent();

        back = (ImageView) findViewById(R.id.back_search);
        userlist = (ListView) findViewById(R.id.list_search);
        useritem = new ArrayList<ChatItem>(){{
        }};
        useradapter = new MySearchAdapter(this,useritem) {
        };
        userlist.setAdapter(useradapter);
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nintent = new Intent(SearchuserActivity.this,InformationActivity.class);
                Bundle nextras = new Bundle();
                nextras.putString("nickname",intent.getExtras().getString("nickname"));
                nextras.putString("target",useradapter.getItem(position).getName());
                nextras.putString("account",intent.getExtras().getString("account"));
                nintent.putExtras(nextras);
                startActivity(nintent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case CHATREFRESH:{
                        useradapter = new MySearchAdapter(getApplicationContext(),useritem) {
                        };
                        userlist.setAdapter(useradapter);
                        break;
                    }
                }
            }
        };



        searchView = (SearchView) findViewById(R.id.search_search);
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
                                    String sql = "select * from user where nickname like '%" + searchView.getQuery().toString() + "%';";
                                    Statement st = (Statement) conn.createStatement();
                                    ResultSet rs = st.executeQuery(sql);
                                    useritem.clear();
                                    while (rs.next()) {
                                        byte [] temp;
                                        temp = Base64.decode(rs.getString("photo"), Base64.DEFAULT);
                                        useritem.add(new ChatItem(BitmapFactory.decodeByteArray(temp, 0, temp.length),rs.getString("nickname"),intent.getExtras().getString("nickname")));
                                        Log.v("aaa",rs.getString("nickname"));
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
                    searchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }
}
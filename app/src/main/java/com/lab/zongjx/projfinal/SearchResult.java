package com.lab.zongjx.projfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    private final int START = 3;
    private MyrecyclerView mRecyclerView;
    private ArrayList<String> publisherSet = new ArrayList();
    private ArrayList<String> titleSet = new ArrayList();
    private ArrayList<String> ddlSet = new ArrayList();
    private ArrayList<String> numSet = new ArrayList();
    private ArrayList< ArrayList<String> > teammaresSet = new ArrayList();
    private ArrayList<String> contentSet = new ArrayList();
    private ArrayList<String> idSet = new ArrayList();
    private MyMainAdapter adapter;
    private Intent intent;
    private FloatingActionButton fab;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case START: {
                    adapter = new MyMainAdapter(intent.getExtras().getString("nickname"), idSet, titleSet, ddlSet, numSet, teammaresSet, contentSet, publisherSet, SearchResult.this);
                    mRecyclerView = (MyrecyclerView)findViewById(R.id.search_recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchResult.this));
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
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        intent = getIntent();
        Log.v("title0",intent.getExtras().getString("key"));

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

                Log.v("title1",intent.getExtras().getString("key"));

                try{Log.v("title2",intent.getExtras().getString("key"));
                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);

                    String sql = "select * from msg where title like '%"+intent.getExtras().getString("key")+"%';";
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

                }
                handler.obtainMessage(START).sendToTarget();
            }
        });
        refreshthread.start();

        fab = (FloatingActionButton)findViewById(R.id.editorFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

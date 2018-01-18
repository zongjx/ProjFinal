package com.lab.zongjx.projfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Integer.parseInt;

public class MyMainAdapter extends RecyclerView.Adapter<MyMainAdapter.PhotoViewHolder> {

    protected ArrayList<String> mtitleSet = new ArrayList();
    protected ArrayList<String> mddlSet = new ArrayList();
    protected ArrayList<String> mnumSet = new ArrayList();
    protected ArrayList<ArrayList<String> > mteammatesSet = new ArrayList();
    protected ArrayList<String> mcontentSet = new ArrayList();
    protected ArrayList<String> midSet = new ArrayList();
    protected ArrayList<String> mpublisherSet = new ArrayList();
    protected String maccountName = new String();

    //protected ArrayList<Map<String,Object>> mData = new ArrayList<Map<String, Object>>();
    //protected String teammates_name = new String();
    //protected int num1;
    //protected int teammates_num;
    //protected boolean flag_join;
    //protected boolean flag_quit;
    //protected boolean flag_clear;
    //protected SimpleAdapter adapter;


    protected final int JOIN = 1;
    protected final int QUIT = 2;

    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private Context mContext;

    public MyMainAdapter(String accountName, ArrayList<String> idSet, ArrayList<String> titleSet, ArrayList<String> ddlSet, ArrayList<String> numSet, ArrayList<ArrayList<String> > teammatesSet, ArrayList<String> contentSet, ArrayList<String> publisherSet, Context context) {
        maccountName = accountName;
        midSet = idSet;
        mtitleSet = titleSet;
        mddlSet = ddlSet;
        mnumSet = numSet;
        mteammatesSet = teammatesSet;
        mcontentSet = contentSet;
        mpublisherSet = publisherSet;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        javabean group = new javabean();

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case JOIN:{
                        if (!group.getFlag_join()){
                            int num1 = group.getNum1();
                            num1--;
                            holder.num.setText(String.valueOf(num1));
                            group.setNum1(num1);
                            mteammatesSet.get(position).add(maccountName);
                            int teammates_num = group.getTeammates_num();
                            teammates_num++;
                            group.setTeammates_num(teammates_num);
                            String teammates_name = group.getTeammates_name();
                            teammates_name += ","+maccountName;
                            holder.teammates.setText(teammates_name);
                            group.setTeammates_name(teammates_name);
                            Map<String,Object> item = new HashMap<String, Object>();
                            item.put("name",maccountName);
                            group.setmData(item);
                            //mData.add(item);
                            //adapter.notifyDataSetChanged();
                            group.getAdapter().notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(holder.name_list);
                            holder.quitBt.setVisibility(View.VISIBLE);
                            holder.joinBt.setVisibility(View.INVISIBLE);
                            holder.already.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                    case QUIT:{
                        if(group.getFlag_clear()){
                            holder.num.setText("");
                            holder.teammates.setText("");
                            group.clear_mdata();
                            //mData.clear();
                            //adapter.notifyDataSetChanged();
                            group.getAdapter().notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(holder.name_list);
                            holder.joinBt.setVisibility(View.INVISIBLE);
                            holder.quitBt.setVisibility(View.INVISIBLE);
                            holder.mFoldableLayout.foldWithAnimation();
                        }
                        else if (group.getFlag_quit()){
                            int num1 = group.getNum1();
                            num1++;
                            holder.num.setText(String.valueOf(num1));
                            group.setNum1(num1);
                            String teammates_name = "";
                            int teammates_num = group.getTeammates_num();
                            if (mteammatesSet.get(position).get(teammates_num-1).equals(maccountName)) {
                                for (int i = 0; i < teammates_num-1; i++) {
                                    if (i != teammates_num - 2) teammates_name += mteammatesSet.get(position).get(i) + ",";
                                    else teammates_name += mteammatesSet.get(position).get(i);
                                }
                                mteammatesSet.get(position).remove(teammates_num-1);
                            }
                            else{
                                int index=0;
                                for (int i = 0; i < teammates_num; i++) {
                                    if (!mteammatesSet.get(position).get(i).equals(maccountName)) {
                                        if (i != teammates_num - 1) teammates_name += mteammatesSet.get(position).get(i) + ",";
                                        else teammates_name += mteammatesSet.get(position).get(i);
                                    }
                                    else index = i;
                                }
                                mteammatesSet.get(position).remove(index);
                            }
                            teammates_num--;
                            group.setTeammates_num(teammates_num);
                            holder.teammates.setText(teammates_name);
                            group.setTeammates_name(teammates_name);
                            ArrayList<Map<String,Object>> mData = group.getmData();
                            for (int i=0;i<mData.size();i++){
                                if (mData.get(i).get("name").toString().equals(maccountName)){
                                    //mData.remove(i);
                                    group.remove_mdata(i);
                                    break;
                                }
                            }
                            //adapter.notifyDataSetChanged();
                            group.getAdapter().notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(holder.name_list);
                            holder.quitBt.setVisibility(View.INVISIBLE);
                            holder.joinBt.setVisibility(View.VISIBLE);
                        }
                        holder.already.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }
        };

        // Bind data
        holder.title.setText(mtitleSet.get(position));
        holder.item_title.setText(mtitleSet.get(position));
        holder.ddl.setText(mddlSet.get(position));
        holder.content.setText(mcontentSet.get(position));

        int teammates_num=mteammatesSet.get(position).size();
        int num1 = parseInt(mnumSet.get(position),10)-teammates_num;
        holder.num.setText(String.valueOf(num1));
        group.setTeammates_num(teammates_num);
        group.setNum1(num1);

        String teammates_name = "";
        for (int i=0;i<teammates_num;i++){
            if (i!=teammates_num-1) teammates_name += mteammatesSet.get(position).get(i) + ",";
            else teammates_name += mteammatesSet.get(position).get(i);
        }
        holder.teammates.setText(teammates_name);
        group.setTeammates_name(teammates_name);

        //ArrayList<Map<String,Object>> mData = new ArrayList<Map<String, Object>>();
        for (int i=0;i<teammates_num;i++){
            Map<String,Object> item = new HashMap<String, Object>();
            item.put("name",mteammatesSet.get(position).get(i));
            Log.v("teammates_NAME"+String.valueOf(i),mteammatesSet.get(position).get(i));
            group.setmData(item);
            //mData.add(item);
        }
        //ArrayList<Map<String,Object>> mData = group.getmData();

        group.setAdapter(new SimpleAdapter(holder.cardView.getContext(),group.getmData(),R.layout.name_listview,new String[] {"name"},new int[] {R.id.name_item}));
        //SimpleAdapter adapter = new SimpleAdapter(holder.cardView.getContext(),group.getmData(),R.layout.name_listview,new String[] {"name"},new int[] {R.id.name_item});
        holder.name_list.setAdapter(group.getAdapter());
        setListViewHeightBasedOnChildren(holder.name_list);
        holder.name_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,InformationActivity.class);
                Bundle nextras = new Bundle();
                Log.v("target2",group.getmData().get(position).get("name").toString());
                nextras.putString("nickname",maccountName);
                nextras.putString("target",group.getmData().get(position).get("name").toString());
                intent.putExtras(nextras);
                mContext.startActivity(intent);
            }
        });

        boolean flag = false;
        for (int i=0;i<teammates_num;i++){
            if (mteammatesSet.get(position).get(i).equals(maccountName)){
                flag=true;
                break;
            }
        }
        if (flag){
            holder.quitBt.setVisibility(View.VISIBLE);
            holder.joinBt.setVisibility(View.INVISIBLE);
            holder.already.setVisibility(View.VISIBLE);
        }
        else if (num1>0){
            holder.quitBt.setVisibility(View.INVISIBLE);
            holder.joinBt.setVisibility(View.VISIBLE);
        }
        else{
            holder.quitBt.setVisibility(View.INVISIBLE);
            holder.joinBt.setVisibility(View.INVISIBLE);
        }
        // Bind state
        if (mFoldStates.containsKey(position)) {
            if (mFoldStates.get(position) == Boolean.TRUE) {
                if (!holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.foldWithoutAnimation();
                }
            } else if (mFoldStates.get(position) == Boolean.FALSE) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithoutAnimation();
                }
            }
        } else {
            holder.mFoldableLayout.foldWithoutAnimation();
        }

        holder.mFoldableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.unfold.setVisibility(View.INVISIBLE);
                    holder.fold.setVisibility(View.VISIBLE);
                    holder.mFoldableLayout.unfoldWithAnimation();
                }
            }
        });

        holder.joinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new  AlertDialog.Builder(mContext);
                builder.setTitle("JOIN");
                builder.setMessage("你确定要加入“"+mtitleSet.get(position)+"”小组?");
                /* 点击了Dialog的确定按钮，则移除该item */
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        group.setFlag_join(false);
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

                                String ip = "120.78.73.208";
                                int port = 3306;
                                String dbName = "zuazu";
                                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                                String USER = "root";
                                String PASSWORD = "123456";

                                try{
                                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                                    Log.v("ss","success");
                                    Statement stmt = (Statement) conn.createStatement();
                                    Log.v("account",maccountName);
                                    String sql = String.format("select * from teammates where msgid = %s and name = '%s';",midSet.get(position),maccountName);
                                    ResultSet rs = stmt.executeQuery(sql);
                                    while(rs.next()){
                                        group.setFlag_join(true);
                                    }
                                    rs.close();
                                    if (!group.getFlag_join()){
                                        sql = String.format("INSERT INTO teammates(msgid,name) VALUES (%s,'%s');",midSet.get(position),maccountName);
                                        stmt.executeUpdate(sql);
                                    }
                                    stmt.close();
                                    conn.close();
                                }catch(SQLException e){
                                    Log.v("ss","fail");
                                    Log.v("ss",e.getMessage());
                                }
                                handler.obtainMessage(JOIN).sendToTarget();
                            }
                        });
                        refreshthread.start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        holder.quitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new  AlertDialog.Builder(mContext);
                builder.setTitle("QUIT");
                builder.setMessage("你确定要退出“"+mtitleSet.get(position)+"”小组?");
                //点击了Dialog的确定按钮，则移除该item
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        group.setFlag_quit(false);
                        group.setFlag_clear(false);
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

                                String ip = "120.78.73.208";
                                int port = 3306;
                                String dbName = "zuazu";
                                String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
                                String USER = "root";
                                String PASSWORD = "123456";

                                try{
                                    Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
                                    Log.v("ss","success");
                                    Statement stmt = (Statement) conn.createStatement();

                                    int teammates_num = group.getTeammates_num();
                                    Log.v("teammates_num",String.valueOf(teammates_num));
                                    if (teammates_num-1<=0){
                                        group.setFlag_clear(true);
                                        Log.v("midset",midSet.get(position));
                                        stmt.executeUpdate("delete from msg where msgid = "+midSet.get(position)+";");
                                        stmt.executeUpdate("delete from teammates where msgid = "+midSet.get(position)+";");
                                    }
                                    else{
                                        String sql = new String();
                                        if (mpublisherSet.get(position).equals(maccountName)){
                                            sql = String.format("update msg set publisher = '%s' where msgid = %s ;",mteammatesSet.get(position).get(1),midSet.get(position));
                                            stmt.executeUpdate(sql);
                                            sql = String.format("select * from teammates where msgid = %s and name = '%s';",midSet.get(position),mteammatesSet.get(position).get(1));
                                            ResultSet rs = stmt.executeQuery(sql);
                                            while(rs.next()){
                                                group.setFlag_quit(true);
                                            }
                                            rs.close();
                                            sql = String.format("delete from teammates where msgid = %s and name = '%s';",midSet.get(position),mteammatesSet.get(position).get(1));
                                        }
                                        else {
                                            Log.v("delete","success");
                                            sql = String.format("select * from teammates where msgid = %s and name = '%s';",midSet.get(position),maccountName);
                                            ResultSet rs = stmt.executeQuery(sql);
                                            while(rs.next()){
                                                group.setFlag_quit(true);
                                            }
                                            rs.close();
                                            sql = String.format("delete from teammates where msgid = %s and name = '%s';",midSet.get(position),maccountName);
                                        }
                                        if (group.getFlag_quit()){
                                            stmt.executeUpdate(sql);
                                        }

                                    }
                                    stmt.close();
                                    conn.close();
                                }catch(SQLException e){
                                    Log.v("ss","fail");
                                    Log.v("SQLException",e.getMessage());
                                }
                                handler.obtainMessage(QUIT).sendToTarget();
                            }
                        });
                        refreshthread.start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        holder.fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unfold.setVisibility(View.VISIBLE);
                holder.fold.setVisibility(View.INVISIBLE);
                holder.mFoldableLayout.foldWithAnimation();
            }
        });

        holder.mFoldableLayout.setFoldListener(new FoldableLayout.FoldListener() {
            @Override
            public void onUnFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onUnFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), false);
            }

            @Override
            public void onFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), true);
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null){
            return;
        }
        int totalHeight = 0;
        for (int i=0,len = listAdapter.getCount();i<len;i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mtitleSet.size();
    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

        protected FoldableLayout mFoldableLayout;

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        @BindView(R.id.item_title)
        protected TextView item_title;

        @BindView(R.id.item_time)
        protected TextView ddl;

        @BindView(R.id.item_num)
        protected TextView num;

        @BindView(R.id.item_name)
        protected TextView teammates;

        @BindView(R.id.already)
        protected ImageView already;

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        @BindView(R.id.content)
        protected TextView content;

        @BindView(R.id.title)
        protected TextView title;

        @BindView(R.id.joinBt)
        protected ImageView joinBt;

        @BindView(R.id.quitBt)
        protected ImageView quitBt;

        @BindView(R.id.fold)
        protected ImageView fold;

        @BindView(R.id.unfold)
        protected ImageView unfold;

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        @BindView(R.id.card)
        protected CardView cardView;

        @BindView(R.id.name_list)
        protected ListView name_list;

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        public PhotoViewHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
    }
}

package com.lab.zongjx.projfinal;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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

public class MyMainAdapter extends RecyclerView.Adapter<MyMainAdapter.PhotoViewHolder> {

    protected ArrayList<String> mtitleSet = new ArrayList();
    protected ArrayList<String> mddlSet = new ArrayList();
    protected ArrayList<String> mnumSet = new ArrayList();
    protected ArrayList<String> mteammatesSet = new ArrayList();
    protected ArrayList<String> mcontentSet = new ArrayList();
    protected ArrayList<String> midSet = new ArrayList();
    protected String maccountName = new String();

    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private Context mContext;

    /*protected void updateDataBase_equit(String msgid,String teammates){
        String connectString = "jdbc:mysql://120.78.73.208:3306/zuazu"
                + "?autoReconnect=true&useUnicode=true"
                + "&characterEncoding=UTF-8";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(connectString,
                    "root", "123456");
            Statement stmt=con.createStatement();
            if (teammates.equals(""))stmt.executeUpdate("delete from msg where msgid = "+msgid+";");
            else stmt.executeUpdate("update msg set num = num - 1 , teammates = "+teammates+" where msgid = "+msgid+";");
            con.close();
            stmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/

   /* protected void updateDataBase_join(String msgid,String teammates){
        String connectString = "jdbc:mysql://120.78.73.208:3306/zuazu"
                + "?autoReconnect=true&useUnicode=true"
                + "&characterEncoding=UTF-8";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(connectString,
                    "root", "123456");
            Statement stmt=con.createStatement();
            stmt.executeUpdate("update msg set num = num + 1 , teammates = "+teammates+" where msgid = "+msgid+";");
            con.close();
            stmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/

    public MyMainAdapter(String accountName, ArrayList<String> idSet, ArrayList<String> titleSet, ArrayList<String> ddlSet, ArrayList<String> numSet, ArrayList<String> teammatesSet, ArrayList<String> contentSet, Context context) {
        maccountName = accountName;
        midSet = idSet;
        mtitleSet = titleSet;
        mddlSet = ddlSet;
        mnumSet = numSet;
        mteammatesSet = teammatesSet;
        mcontentSet = contentSet;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new FoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        // Bind data
        holder.title.setText(mtitleSet.get(position));
        holder.item_title.setText(mtitleSet.get(position));
        holder.ddl.setText(mddlSet.get(position));
        holder.num.setText(mnumSet.get(position));
        holder.teammates.setText(mteammatesSet.get(position));
        holder.content.setText(mcontentSet.get(position));
        holder.scrollView.requestDisallowInterceptTouchEvent(true);

        String[] ss = mteammatesSet.get(position).split(",");
        boolean flag = false;
        for (int i=0;i<ss.length;i++){
            if (ss[i].equals(maccountName)){
                flag=true;
                break;
            }
        }
        if (flag){
            holder.equitBt.setVisibility(View.VISIBLE);
            holder.joinBt.setVisibility(View.INVISIBLE);
        }
        else{
            holder.equitBt.setVisibility(View.INVISIBLE);
            holder.joinBt.setVisibility(View.VISIBLE);
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
                    holder.mFoldableLayout.unfoldWithAnimation();
                    holder.cardView.setFocusable(true);
                    holder.cardView.setFocusableInTouchMode(true);
                    holder.cardView.requestFocus();
                    holder.cardView.requestFocusFromTouch();
                }
            }
        });

        //holder.equitBt.setVisibility(View.INVISIBLE);
        holder.joinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mteammatesSet.get(position) + ","+maccountName;
                //updateDataBase_join(midSet.get(position),s);
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

                            stmt.executeUpdate("update msg set num = num + 1 , teammates = "+s+" where msgid = "+midSet.get(position)+";");
                            stmt.close();
                            conn.close();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                refreshthread.start();
                holder.equitBt.setVisibility(View.VISIBLE);
                holder.joinBt.setVisibility(View.INVISIBLE);
            }
        });
        holder.equitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //updateDataBase_equit(midSet.get(position),s);
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
                            String[] ss = mteammatesSet.get(position).split(",");
                            String s = "";
                            for (int i=0;i<ss.length;i++){
                                if (!ss[i].equals(maccountName)){
                                    if (s.equals("")) s += ss[i];
                                    else s += ","+ss[i];
                                }
                            }
                            if (s.equals(""))stmt.executeUpdate("delete from msg where msgid = "+midSet.get(position)+";");
                            else stmt.executeUpdate("update msg set num = num - 1 , teammates = "+s+" where msgid = "+midSet.get(position)+";");
                            stmt.close();
                            conn.close();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                refreshthread.start();
                holder.equitBt.setVisibility(View.INVISIBLE);
                holder.joinBt.setVisibility(View.VISIBLE);
            }
        });

        holder.fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        @BindView(R.id.content)
        protected TextView content;

        @BindView(R.id.title)
        protected TextView title;

        @BindView(R.id.joinBt)
        protected Button joinBt;

        @BindView(R.id.equitBt)
        protected Button equitBt;

        @BindView(R.id.fold)
        protected Button fold;

/*-----------------------------------------------------------------------------------
        -------------------------------------------------------------------------------------*/

        @BindView(R.id.scroll)
        protected ScrollView scrollView;

@BindView(R.id.card)
protected CardView cardView;


        public PhotoViewHolder(FoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.list_item_cover, R.layout.list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
    }
}

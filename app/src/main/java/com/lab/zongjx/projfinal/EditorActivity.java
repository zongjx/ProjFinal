package com.lab.zongjx.projfinal;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity{

    protected String accountName = new String();
    protected String title = new String();
    protected String content = new String();
    protected String ddl = new String();
    protected String num = new String();

    @BindView(R.id.editTitle)
    protected EditText editTitle;

    @BindView(R.id.editContent)
    protected EditText editContent;

    @BindView(R.id.editDDL)
    protected EditText editDDL;

    @BindView(R.id.editNumber)
    protected EditText editNumber;

    @BindView(R.id.send)
    protected Button send;

    /* protected void insertDataBase(){
         String connectString = "jdbc:mysql://120.78.73.208:3306/zuazu"
                 + "?autoReconnect=true&useUnicode=true"
                 + "&characterEncoding=UTF-8";
         try{
             Class.forName("com.mysql.jdbc.Driver");
             Connection con= DriverManager.getConnection(connectString,
                     "root", "123456");
             Statement stmt=con.createStatement();

             String sql = String.format("INSERT INTO msg(publisher,title,ddl,num,content) VALUES ('%s','%s',%s,'%s');",title,ddl,num,content);
             stmt.executeUpdate(sql);

             con.close();
             stmt.close();
         }catch (Exception ex){
             ex.printStackTrace();
         }
     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);

        accountName = getIntent().getExtras().getString("accountName");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTitle.getText().toString();
                content = editContent.getText().toString();
                ddl = editDDL.getText().toString();
                num = editNumber.getText().toString();
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
                            String sql = String.format("INSERT INTO msg(publisher,title,ddl,num,content) VALUES ('%s','%s','%s',%s,'%s');",accountName,title,ddl,num,content);
                            stmt.executeUpdate(sql);
                            stmt.close();
                            conn.close();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                refreshthread.start();
                setResult(1);
            }
        });

    }
}

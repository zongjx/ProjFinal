package com.lab.zongjx.projfinal;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity{

    protected String accountName = new String();
    protected String title = new String();
    protected String content = new String();
    protected String ddl = new String();
    protected String num = new String();
    private final int SUCCESS = 1;

    @BindView(R.id.editTitle)
    protected EditText editTitle;

    @BindView(R.id.editContent)
    protected EditText editContent;

    @BindView(R.id.editDDL)
    protected EditText editDDL;

    @BindView(R.id.editNumber)
    protected EditText editNumber;

    @BindView(R.id.send)
    protected ImageView send;

    @BindView(R.id.settingdate)
    protected ImageView date;

    @BindView(R.id.back)
    protected ImageView back;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ButterKnife.bind(this);

        accountName = getIntent().getExtras().getString("accountName");

        //初始化Calendar日历对象
        Calendar mycalendar=Calendar.getInstance();
        year=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建DatePickerDialog对象
                DatePickerDialog dpd=new DatePickerDialog(EditorActivity.this,null,year,month,day);
                //手动设置按钮
                dpd.setButton(DialogInterface.BUTTON_POSITIVE,"完成",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                        DatePicker datePicker = dpd.getDatePicker();
                        year = datePicker.getYear();
                        month = datePicker.getMonth();
                        day = datePicker.getDayOfMonth();
                        editDDL.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
                    }
                });
                //取消按钮，如果不需要直接不设置即可
                dpd.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){

                    }
                });
                dpd.show();//显示DatePickerDialog组件
            }
        });

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case SUCCESS:{
                        Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTitle.getText().toString();
                content = editContent.getText().toString();
                ddl = editDDL.getText().toString();
                num = editNumber.getText().toString();
                if (title.equals(null) || title == null|| title.equals("")){
                    Toast.makeText(EditorActivity.this, "请填写Title信息", Toast.LENGTH_SHORT).show();
                }
                else if (content.equals(null) || content == null|| content.equals("")){
                    Toast.makeText(EditorActivity.this, "请填写Content信息", Toast.LENGTH_SHORT).show();
                }
                else if (ddl.equals(null) || ddl == null|| ddl.equals("")){
                    Toast.makeText(EditorActivity.this, "请填写DDL信息", Toast.LENGTH_SHORT).show();
                }
                else if (num.equals(null) || num == null|| num.equals("")){
                    Toast.makeText(EditorActivity.this, "请填写Number信息", Toast.LENGTH_SHORT).show();
                }
                else {
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
                                handler.obtainMessage(SUCCESS).sendToTarget();
                                finish();
                            }catch(SQLException e){
                                Log.v("ss","fail");
                                Log.v("ss",e.getMessage());
                            }
                        }
                    });
                    refreshthread.start();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

   /* private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener()
    {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        /*@Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {

            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year=myyear;
            month=monthOfYear;
            day=dayOfMonth;
            //更新日期
            updateDate();
        }
        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate()
        {
            //在TextView上显示日期
            editDDL.setText(year+"-"+(month+1)+"-"+day);
        }
    };*/
}

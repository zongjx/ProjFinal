package com.lab.zongjx.projfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Upload extends AppCompatActivity {

    private Button upload;
    private Button down;
    private ImageView photo;
    private Uri imageUri;
    public static final int CROP_PHOTO = 2;
    private byte[] in;
    private String store;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        upload = (Button) findViewById(R.id.upload);
        down = (Button) findViewById(R.id.download);
        photo = (ImageView) findViewById(R.id.photo);


        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 123:{
                        photo.setImageBitmap(bitmap);
                        break;
                    }
                }
            }
        };

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                            String sql = "select * from photo where id = '2';";
                            Statement st = (Statement) conn.createStatement();
                            ResultSet rs = st.executeQuery(sql);

                            byte[] bitmapArray;
                            if(rs.next()){
                                bitmapArray = Base64.decode(rs.getString("photo1"), Base64.DEFAULT);
                                bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0,
                                                bitmapArray.length);
                                handler.obtainMessage(123).sendToTarget();
                            }

                            rs.close();
                            st.close();
                            conn.close();
                        }catch(SQLException e){
                            Log.v("ss","fail");
                            Log.v("ss",e.getMessage());
                        }
                    }
                });
                thread.start();
            }
        });

        in = null;
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.
                        getExternalStorageDirectory(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,CROP_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CROP_PHOTO){
            if (resultCode == RESULT_OK) {
                try {
                    if(data != null){
                        imageUri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap bm = Bitmap.createScaledBitmap(bitmap,100,100,true);
                        photo.setImageBitmap(bm); // 将裁剪后的照片显示出来
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        in = baos.toByteArray();
                        store = Base64.encodeToString(in, Base64.DEFAULT);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                                        String sql = "insert into photo(photo) values('"+ store +"');";
                                        Statement st = (Statement) conn.createStatement();
                                        st.executeUpdate(sql);
                                        st.close();
                                        conn.close();
                                    }catch(SQLException e){
                                        Log.v("ss","fail");
                                        Log.v("ss",e.getMessage());
                                    }
                                }
                        });
                        thread.start();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

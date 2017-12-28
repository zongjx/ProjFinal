package com.lab.zongjx.projfinal;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import tslamic.fancybg.FancyBackground;

public class LoginActivity extends AppCompatActivity implements FancyBackground.FancyListener{
    private static final String TAG = "FancyBackground";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final View view = findViewById(R.id.parent);

        FancyBackground.on(view)
                .set(R.mipmap.fbg_fst, R.mipmap.fbg_snd, R.mipmap.fbg_trd)
                .inAnimation(R.anim.fade_in)
                .outAnimation(R.anim.fade_out)
                .interval(2500)
                .start();
    }



    @Override
    public void onStarted(FancyBackground bg) {
        Log.d(TAG, "Started FancyBackground.");
    }

    @Override
    public void onNew(FancyBackground bg) {
        Log.d(TAG, "New pic loaded.");
    }

    @Override
    public void onStopped(FancyBackground bg) {
        Log.d(TAG, "Stopped FancyBackground.");
    }

    @Override
    public void onLoopDone(FancyBackground bg) {
        Log.d(TAG, "Loop complete.");
    }

}

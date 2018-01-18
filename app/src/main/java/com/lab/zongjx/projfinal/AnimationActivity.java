package com.lab.zongjx.projfinal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AnimationActivity extends AppCompatActivity {
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private ImageButton enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        text1 = (TextView) findViewById(R.id.text1_animation);
        text2 = (TextView) findViewById(R.id.text2_animation);
        //enter = (ImageButton) findViewById(R.id.enter_animation);

        text1.setTypeface(Typeface.createFromAsset(getAssets(), "ziti2.ttf"));
        text2.setTypeface(Typeface.createFromAsset(getAssets(), "ziti1.ttf"));

        AnimationSet animationSet1 = new AnimationSet(true);
        AnimationSet animationSet2 = new AnimationSet(true);
        //AnimationSet animationSet4 = new AnimationSet(true);

        final AlphaAnimation alphaAnimation1 = new AlphaAnimation(0,1);
        final AlphaAnimation alphaAnimation2 = new AlphaAnimation(0,1);
        //final AlphaAnimation alphaAnimation4 = new AlphaAnimation(0,1);

        alphaAnimation1.setDuration(1000);
        alphaAnimation2.setDuration(1000);
        //alphaAnimation4.setDuration(1000);

        final TranslateAnimation translateAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,2f,Animation.RELATIVE_TO_SELF,0f);
        final TranslateAnimation translateAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,2f,Animation.RELATIVE_TO_SELF,0f);
        //final TranslateAnimation translateAnimation4 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,2f,Animation.RELATIVE_TO_SELF,0f);

        translateAnimation1.setDuration(1000);
        translateAnimation2.setDuration(1000);
        //translateAnimation4.setDuration(1000);

        animationSet1.addAnimation(alphaAnimation1);
        animationSet1.addAnimation(translateAnimation1);
        animationSet2.addAnimation(alphaAnimation2);
        animationSet2.addAnimation(translateAnimation2);
       // animationSet4.addAnimation(alphaAnimation4);
        //animationSet4.addAnimation(translateAnimation4);

        animationSet1.setFillAfter(true);
        animationSet2.setFillAfter(true);
       // animationSet4.setFillAfter(true);

        final AlphaAnimation alphaAnimation5 = new AlphaAnimation(0,0);

        alphaAnimation5.setDuration(100);
        alphaAnimation5.setFillAfter(true);

        text2.startAnimation(alphaAnimation5);
//        enter.startAnimation(alphaAnimation5);

        text1.setAnimation(animationSet1);
        animationSet1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                text2.setAnimation(animationSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animationSet2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //enter.setAnimation(animationSet4);
                new Thread(){
                    public void run(){
                        try{
                            sleep(3000);
                            Intent intent = new Intent(AnimationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (InterruptedException e){

                        }
                    }
                }.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}

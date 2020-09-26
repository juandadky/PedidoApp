package com.warsoft.pedidoapp.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;

public class ScreenSplashActivity extends AppCompatActivity {

    private ImageView imgSplash;
    private SharedPreferences preferences;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(preferences.contains(Util.RECORDAR)){
                if(preferences.getInt(Util.RECORDAR,-1) == 1) {
                    startActivity(new Intent(ScreenSplashActivity.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(ScreenSplashActivity.this,LoginActivity.class));
                    finish();
                }
            }else{
                startActivity(new Intent(ScreenSplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_splash);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        imgSplash = findViewById(R.id.img_splash);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.splashscreen);
        imgSplash.startAnimation(myAnim);
    }

    @Override
    protected void onStart() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendMessage(handler.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        super.onStart();
    }

}

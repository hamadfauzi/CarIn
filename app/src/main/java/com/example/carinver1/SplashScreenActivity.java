package com.example.carinver1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    TextView dicari,mudah;
    ImageView logo,imageSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.my_transsition);
        dicari.startAnimation(myanim);
        mudah.startAnimation(myanim);
        logo.startAnimation(myanim);
        imageSplash.startAnimation(myanim);
        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
    public void initialize(){
        dicari = (TextView) findViewById(R.id.textDicariin);
        mudah = (TextView) findViewById(R.id.cepat);
        logo = (ImageView) findViewById(R.id.logocarin);
        imageSplash = (ImageView) findViewById(R.id.splashImage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

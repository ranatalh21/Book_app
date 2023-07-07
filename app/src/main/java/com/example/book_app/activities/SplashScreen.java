package com.example.book_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.book_app.R;
//import com.example.instagramlikeapp.R;

public class SplashScreen extends AppCompatActivity {

}


//    TextView txtInstagram;
//    RelativeLayout relativeLayout;
//    Animation textAnimation, layoutAnimation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//
//
//
//
//        //this is the varilable for text in the splash
//        txtInstagram= findViewById(R.id.txtInsta);
//        relativeLayout= findViewById(R.id.realitive);
//
//
//
//        textAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fall_down);
//        layoutAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_to_top);
//
//        //This handler use to animate the layout
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                relativeLayout.setVisibility(View.VISIBLE);
//                relativeLayout.setAnimation(layoutAnimation);
//
//                //This handel use to animate the text
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        txtInstagram.setVisibility(View.VISIBLE);
//
//                        txtInstagram.setAnimation(textAnimation);
//                    }
//                }, 900);
//
//            }
//        }, 500);
//        //This handel use to animate the splash icon
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        }, 6000);
//    }
//}

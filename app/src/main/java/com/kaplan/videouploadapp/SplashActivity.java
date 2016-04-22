package com.kaplan.videouploadapp;

import com.kaplan.videouploadapp.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Fatih Kaplan on 19/04/16.
 */
@EActivity(R.layout.splash_activity)
public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @AfterViews
    protected void afterViews() {
//        initUI();
    }
//
//    @UiThread
//    public void initUI() {
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                Intent i = new Intent(SplashActivity.this, MainActivity_.class);
//                startActivity(i);
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
//    }
}

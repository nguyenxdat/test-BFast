package vn.datnx.todolist.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import vn.datnx.todolist.R;
import vn.datnx.todolist.utils.Constants;


public class ActivitySplashScreen extends ActivityBase {

    private static final long SPLASH_TIME_OUT = 1500;
    private SharedPreferences sharedPreferences;


    @Override
    protected int getLayoutView() {
        return R.layout.splash_screen_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        changeStatusBarColor();
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!sharedPreferences.getBoolean(Constants.LOGIN_SUCCESS, false)) {
                    openLogin();
                } else {
                    openMain();
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void openLogin() {
        startActivity(new Intent(ActivitySplashScreen.this, ActivityLogin.class));
    }

    private void openMain() {
        startActivity(new Intent(ActivitySplashScreen.this, ActivityMain.class));
    }

}

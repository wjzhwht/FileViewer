package com.xerofox.fileviewer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.xerofox.fileviewer.MainActivity;
import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.helper.SettingHelper;
import com.xerofox.fileviewer.ui.common.BaseActivity;

public class SplashActivity extends BaseActivity {
    private static final int UI_ANIMATION_DELAY = 300;
    private static final int START_DELAY = 2000;
    private final Handler mHandler = new Handler();
    private final Runnable mShowPart2Runnable = () -> {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private final Runnable mHideRunnable = this::hide;

    private final Runnable mStartRunnable = this::start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
        delayedStart();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHandler.removeCallbacks(mShowPart2Runnable);
    }

    private void delayedHide(int delayMillis) {
        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void delayedStart() {
        mHandler.removeCallbacks(mStartRunnable);
        mHandler.postDelayed(mStartRunnable, START_DELAY);
    }

    private void start() {
        if (!SettingHelper.isServerPortSetted()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}

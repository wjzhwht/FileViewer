package com.xerofox.fileviewer;

import android.app.Application;

import com.xerofox.fileviewer.util.Util;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
    }
}

package com.xerofox.fileviewer;

import com.xerofox.fileviewer.di.AppComponent;
import com.xerofox.fileviewer.di.DaggerAppComponent;
import com.xerofox.fileviewer.util.Util;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class ViewerApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}

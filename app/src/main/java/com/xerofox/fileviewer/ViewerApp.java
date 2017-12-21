package com.xerofox.fileviewer;

import android.os.Environment;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.xerofox.fileviewer.di.AppComponent;
import com.xerofox.fileviewer.di.DaggerAppComponent;
import com.xerofox.fileviewer.util.Util;

import java.io.File;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class ViewerApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "log";
        Printer printer = new FilePrinter.Builder(path).build();
        XLog.init(printer);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}

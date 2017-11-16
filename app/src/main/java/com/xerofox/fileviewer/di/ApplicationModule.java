package com.xerofox.fileviewer.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class ApplicationModule {

    @Binds
    abstract Context bindContext(Application application);
}

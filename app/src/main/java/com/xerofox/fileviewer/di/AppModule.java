package com.xerofox.fileviewer.di;

import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.api.XeroApiImpl;
import com.xerofox.fileviewer.helper.LocalFileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    XeroApi provideXeroApi(){
        return new XeroApiImpl();
    }

    @Singleton
    @Provides
    LocalFileHelper provideLocalFileHelper(){
        return new LocalFileHelper();
    }
}

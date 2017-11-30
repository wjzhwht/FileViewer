package com.xerofox.fileviewer.di;

import android.databinding.DataBindingComponent;

import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.api.XeroApiImpl;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.helper.LocalFileHelper;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module(includes = {ApplicationModule.class})
abstract public class AppModule {

    @Singleton
    @Binds
    abstract XeroApi provideXeroApi(XeroApiImpl xeroApi);

    @Singleton
    @Binds
    abstract FileHelper provideLocalFileHelper(LocalFileHelper fileHelper);

    @Singleton
    @Binds
    abstract DataBindingComponent bindDatabindingComponent(FragmentDataBindingComponent fragmentDataBindingComponent);
}

package com.xerofox.fileviewer.di;

import com.xerofox.fileviewer.MainActivity;
import com.xerofox.fileviewer.ui.viewer.ViewerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

//    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
//    abstract ViewerActivity contributeViewerActivity();
}

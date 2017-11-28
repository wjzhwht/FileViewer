package com.xerofox.fileviewer.di;

import com.xerofox.fileviewer.MainActivity;
import com.xerofox.fileviewer.ui.SplashActivity;
import com.xerofox.fileviewer.ui.index.MainModule;
import com.xerofox.fileviewer.ui.part.TowerPartActivity;
import com.xerofox.fileviewer.ui.part.TowerPartModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TowerPartModule.class)
    abstract TowerPartActivity contributeTowerPartActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();
}

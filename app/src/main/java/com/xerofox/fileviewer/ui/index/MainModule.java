package com.xerofox.fileviewer.ui.index;

import com.xerofox.fileviewer.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();
}

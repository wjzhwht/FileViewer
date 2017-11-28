package com.xerofox.fileviewer.ui.index;

import com.xerofox.fileviewer.di.FragmentScoped;
import com.xerofox.fileviewer.ui.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FunctionFragment functionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();
}

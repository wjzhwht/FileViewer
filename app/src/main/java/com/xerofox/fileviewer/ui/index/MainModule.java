package com.xerofox.fileviewer.ui.index;

import com.xerofox.fileviewer.di.FragmentScoped;
import com.xerofox.fileviewer.ui.download.DownloadFragment;
import com.xerofox.fileviewer.ui.settings.SettingsFragment;
import com.xerofox.fileviewer.ui.task.TaskFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract TaskFragment searchFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FunctionFragment functionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DownloadFragment downloadFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();
}

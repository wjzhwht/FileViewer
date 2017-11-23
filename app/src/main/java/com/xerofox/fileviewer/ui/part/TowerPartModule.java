package com.xerofox.fileviewer.ui.part;

import com.xerofox.fileviewer.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract public class TowerPartModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract FilterDialogFragment filterDialogFragment();
}

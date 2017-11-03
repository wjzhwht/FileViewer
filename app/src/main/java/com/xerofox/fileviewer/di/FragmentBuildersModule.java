package com.xerofox.fileviewer.di;

import com.xerofox.fileviewer.ui.tower.TowerPartFragment;
import com.xerofox.fileviewer.ui.search.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract TowerPartFragment contributeTowerPartFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
